from rest_framework.views import APIView
from rest_framework.response import Response
from rest_framework import permissions
from django.db.models import Q
from .models import Biomarker, ReferenceRange
from rest_framework import generics
from drf_spectacular.utils import extend_schema
from .models import AnalysisRecord
from .serializers import AnalysisRecordSerializer

class PersonalNormsView(APIView):
    permission_classes = [permissions.IsAuthenticated]

    def get(self, request):
        user_profile = request.user.profile
        age = user_profile.age or 30
        gender = user_profile.gender
        weight = user_profile.weight

        norms_data = []

        biomarkers = Biomarker.objects.all()
        for bio in biomarkers:
            ref = ReferenceRange.objects.filter(
                biomarker=bio,
                min_age__lte=age,
                max_age__gte=age
            ).filter(Q(gender=gender) | Q(gender='A')).first()

            if not ref:
                continue

            min_val = ref.min_value
            max_val = ref.max_value

            if bio.name == 'Глюкоза' and weight and weight > 90:
                max_val += 0.3

            norms_data.append({
                'id': bio.id,
                'name': bio.name,
                'unit': bio.unit,
                'min_value': round(min_val, 2),
                'max_value': round(max_val, 2),
            })

        return Response(norms_data)

class AnalysisRecordView(generics.ListCreateAPIView):
    serializer_class = AnalysisRecordSerializer
    permission_classes = [permissions.IsAuthenticated]

    @extend_schema(summary="Получить всю историю анализов пользователя")
    def get_queryset(self):
        return AnalysisRecord.objects.filter(user=self.request.user).prefetch_related('results').order_by('-date')

    @extend_schema(summary="Добавить результаты анализов (Ручной ввод)")
    def perform_create(self, serializer):
        serializer.save(user=self.request.user)
from rest_framework import generics, permissions
from .models import UserProfile
from .serializers import UserProfileSerializer
from rest_framework.response import Response

class UserProfileView(generics.RetrieveUpdateAPIView):
    serializer_class = UserProfileSerializer
    permission_classes = [permissions.IsAuthenticated]

    def patch(self, request, *args, **kwargs):
        print("\n=== ПРИШЛО ОТ МОБИЛКИ ===")
        print(request.data)

        serializer = self.get_serializer(self.get_object(), data=request.data, partial=True)
        if not serializer.is_valid():
            print("=== ОШИБКИ ВАЛИДАЦИИ ===")
            print(serializer.errors)
            return Response(serializer.errors, status=400)

        return super().patch(request, *args, **kwargs)

    def get_object(self):
        return self.request.user.profile
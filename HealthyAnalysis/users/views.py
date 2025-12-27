import random
from django.core.cache import cache
from django.core.mail import send_mail
from django.conf import settings
from rest_framework.views import APIView
from rest_framework.response import Response
from rest_framework import status
from .serializers import UserRegisterSerializer, VerifyEmailSerializer
from django.contrib.auth import get_user_model
from drf_spectacular.utils import extend_schema, OpenApiParameter
from drf_spectacular.types import OpenApiTypes

User = get_user_model()


class RegisterView(APIView):
    @extend_schema(
        summary="Регистрация пользователя",
        description="Принимает email/password. Отправляет код подтверждения на почту. Возвращает 201, если всё ок.",
        request=UserRegisterSerializer,
        responses={201: {"message": "Код отправлен на email"}, 400: "Ошибки валидации"}
    )

    def post(self, request):
        serializer = UserRegisterSerializer(data=request.data)
        if serializer.is_valid():
            user = serializer.save()

            code = random.randint(100000, 999999)

            cache.set(f'verify_email_{user.email}', code, timeout=300)

            send_mail(
                'Подтверждение регистрации',
                f'Ваш код подтверждения: {code}',
                'noreply@healthyapp.com',
                [user.email],
                fail_silently=False,
            )

            return Response({'message': 'Код отправлен на email'}, status=status.HTTP_201_CREATED)
        return Response(serializer.errors, status=status.HTTP_400_BAD_REQUEST)


class VerifyCodeView(APIView):
    @extend_schema(
        summary="Подтверждение почты",
        description="Принимает email и 6-значный код. Активирует аккаунт.",
        request=VerifyEmailSerializer,
        responses={
            200: {"message": "Аккаунт подтвержден!"},
            400: {"error": "Неверный код"},
            404: {"error": "Пользователь не найден"}
        }
    )

    def post(self, request):
        serializer = VerifyEmailSerializer(data=request.data)
        if serializer.is_valid():
            email = serializer.validated_data['email']
            code = serializer.validated_data['code']

            cached_code = cache.get(f'verify_email_{email}')

            if cached_code and str(cached_code) == code:
                try:
                    user = User.objects.get(email=email)
                    user.is_active = True
                    user.save()
                    cache.delete(f'verify_email_{email}')
                    return Response({'message': 'Аккаунт подтвержден!'}, status=status.HTTP_200_OK)
                except User.DoesNotExist:
                    return Response({'error': 'Пользователь не найден'}, status=status.HTTP_404_NOT_FOUND)
            else:
                return Response({'error': 'Неверный или просроченный код'}, status=status.HTTP_400_BAD_REQUEST)

        return Response(serializer.errors, status=status.HTTP_400_BAD_REQUEST)
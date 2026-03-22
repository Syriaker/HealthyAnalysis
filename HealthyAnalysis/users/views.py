import random
from django.core.cache import cache
import threading
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

class EmailThread(threading.Thread):
    def __init__(self, subject, message, recipient_list):
        self.subject = subject
        self.message = message
        self.recipient_list = recipient_list
        threading.Thread.__init__(self)

    def run(self):
        send_mail(
            self.subject,
            self.message,
            settings.DEFAULT_FROM_EMAIL,
            self.recipient_list,
            fail_silently=False,
        )

class RegisterView(APIView):
    @extend_schema(
        summary="Регистрация пользователя",
        description="Принимает email/password. Отправляет код подтверждения на почту. Возвращает 201, если всё ок.",
        request=UserRegisterSerializer,
        responses={201: {"message": "Код отправлен на email"}, 400: "Ошибки валидации"}
    )
    def post(self, request):
        email = request.data.get('email')
        password = request.data.get('password')

        # 1. Проверяем, есть ли уже такой пользователь
        try:
            user = User.objects.get(email=email)
            if user.is_active:
                # Пользователь есть и подтвержден - ошибка
                return Response({'error': 'Пользователь с таким email уже существует.'},
                                status=status.HTTP_400_BAD_REQUEST)
            else:
                # Пользователь есть, но почту НЕ подтвердил.
                # Обновляем ему пароль и будем отправлять новый код
                user.set_password(password)
                user.save()

        except User.DoesNotExist:
            # 2. Пользователя нет вообще - создаем нового штатным способом
            serializer = UserRegisterSerializer(data=request.data)
            if serializer.is_valid():
                user = serializer.save()
            else:
                return Response(serializer.errors, status=status.HTTP_400_BAD_REQUEST)

        # 3. Генерируем код (для обоих случаев: и для новых, и для старых неподтвержденных)
        code = random.randint(100000, 999999)

        # Переводим код в строку перед сохранением в кэш
        cache.set(f'verify_email_{user.email}', str(code), timeout=300)

        # 4. Отправляем письмо в фоне (без зависаний сервера)
        EmailThread(
            'Подтверждение регистрации Здоровый Анализ',
            f'Ваш код подтверждения: {code}',
            [user.email]
        ).start()

        # 5. Мгновенно отвечаем фронтенду
        return Response({'message': 'Код отправлен на email'}, status=status.HTTP_201_CREATED)

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
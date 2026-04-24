import requests
from rest_framework.views import APIView
from rest_framework import generics, permissions
from rest_framework.response import Response
from drf_spectacular.utils import extend_schema, OpenApiParameter
from .models import Product, FoodLog
from .serializers import ProductSerializer, FoodLogSerializer, CustomProductSerializer
from django.utils import timezone

class ScanProductView(APIView):
    permission_classes = [permissions.IsAuthenticated]

    @extend_schema(
        summary="Сканировать штрих-код",
        parameters=[OpenApiParameter(name='barcode', description='Штрих-код продукта', required=True, type=str)],
        responses={200: ProductSerializer, 404: "Продукт не найден"}
    )
    def get(self, request):
        barcode = request.query_params.get('barcode')

        if not barcode:
            return Response({'error': 'Укажите параметр barcode'}, status=400)
        try:
            product = Product.objects.get(barcode=barcode)
            return Response(ProductSerializer(product).data)
        except Product.DoesNotExist:
            pass

        off_url = f"https://world.openfoodfacts.org/api/v0/product/{barcode}.json"

        headers = {
            'User-Agent': 'HealthyAnalysisApp - Android/iOS - Version 1.0'
        }

        try:
            response = requests.get(off_url, headers=headers, timeout=10)

            if response.status_code != 200:
                print(f"OFF вернул ошибку: {response.status_code}")
                return Response({'error': 'Сервер продуктов временно недоступен'}, status=503)

            data = response.json()

            if data.get('status') == 1:
                off_product = data.get('product', {})
                nutriments = off_product.get('nutriments', {})

                new_product = Product.objects.create(
                    barcode=barcode,
                    name=off_product.get('product_name', 'Неизвестный продукт'),
                    image_url=off_product.get('image_front_url', ''),
                    calories=nutriments.get('energy-kcal_100g', 0) or 0,
                    proteins=nutriments.get('proteins_100g', 0) or 0,
                    fats=nutriments.get('fat_100g', 0) or 0,
                    carbs=nutriments.get('carbohydrates_100g', 0) or 0
                )
                return Response(ProductSerializer(new_product).data)
            else:
                return Response({'error': 'Продукт не найден в глобальной базе'}, status=404)

        except Exception as e:
            print(f"ОШИБКА OPEN FOOD FACTS: {e}")
            return Response({'error': 'Ошибка соединения с сервером продуктов'}, status=503)


class FoodLogView(generics.ListCreateAPIView):
    serializer_class = FoodLogSerializer
    permission_classes = [permissions.IsAuthenticated]

    @extend_schema(
        summary="Дневник питания (с фильтром по дате)",
        description="Если не передать date, вернет еду за СЕГОДНЯ (имитация сброса в 00:00).",
        parameters=[
            OpenApiParameter(name='date', description='Дата в формате YYYY-MM-DD', required=False, type=str)
        ]
    )
    def get_queryset(self):
        user = self.request.user
        date_str = self.request.query_params.get('date')

        if date_str:
            return FoodLog.objects.filter(user=user, created_at__date=date_str).order_by('-created_at')

        today = timezone.localtime().date()
        return FoodLog.objects.filter(user=user, created_at__date=today).order_by('-created_at')

    def perform_create(self, serializer):
        serializer.save(user=self.request.user)

class CustomProductCreateView(generics.CreateAPIView):
    serializer_class = CustomProductSerializer
    permission_classes = [permissions.IsAuthenticated]

    @extend_schema(
        summary="Создать свой продукт (ручной ввод)",
        description="Создает продукт без штрих-кода и возвращает его ID для добавления в дневник."
    )
    def perform_create(self, serializer):
        serializer.save()
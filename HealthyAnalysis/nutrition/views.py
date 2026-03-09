import requests
from rest_framework.views import APIView
from rest_framework import generics, permissions
from rest_framework.response import Response
from drf_spectacular.utils import extend_schema, OpenApiParameter
from .models import Product, FoodLog
from .serializers import ProductSerializer, FoodLogSerializer


class ScanProductView(APIView):
    permission_classes = [permissions.IsAuthenticated]

    @extend_schema(
        summary="Сканировать штрих-код",
        description="Ищет продукт в локальной БД. Если нет - берет из OpenFoodFacts и сохраняет.",
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

        try:
            response = requests.get(off_url, timeout=5)
            data = response.json()

            if data.get('status') == 1:
                off_product = data['product']
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

        except requests.exceptions.RequestException:
            return Response({'error': 'Ошибка соединения с OpenFoodFacts'}, status=503)

class FoodLogCreateView(generics.CreateAPIView):
    serializer_class = FoodLogSerializer
    permission_classes = [permissions.IsAuthenticated]

    @extend_schema(summary="Добавить продукт в дневник питания")
    def perform_create(self, serializer):
        serializer.save(user=self.request.user)


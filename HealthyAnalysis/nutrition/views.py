import requests
from rest_framework.views import APIView
from rest_framework import generics, permissions
from rest_framework.response import Response
from drf_spectacular.utils import extend_schema, OpenApiParameter
from .models import Product, FoodLog, DailyWater
from .serializers import ProductSerializer, FoodLogSerializer, CustomProductSerializer, DailyWaterSerializer
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
        description="Если не передать date, вернет еду за СЕГОДНЯ. Можно добавлять еду в прошлое передав date при POST.",
        parameters = [
        OpenApiParameter(name='date', description='Дата в формате YYYY-MM-DD', required=False, type=str)
    ]
    )
    def get_queryset(self):
        user = self.request.user
        date_str = self.request.query_params.get('date')
        if date_str:
            return FoodLog.objects.filter(user=user, date=date_str).order_by('-created_at')

        today = timezone.localdate()
        return FoodLog.objects.filter(user=user, date=today).order_by('-created_at')

    def perform_create(self, serializer):
        serializer.save(user=self.request.user)

class GlobalDishSearchView(generics.ListAPIView):
    serializer_class = ProductSerializer
    permission_classes = [permissions.IsAuthenticated]

    @extend_schema(
        summary="Поиск по глобальной библиотеке блюд",
        parameters=[OpenApiParameter(name='q', description='Название блюда (например: борщ)', required=True, type=str)]
    )
    def get_queryset(self):
        query = self.request.query_params.get('q', '')

        if query:
            return Product.objects.filter(is_global_dish=True, name__icontains=query).order_by('-id')[
                   :30]
        return Product.objects.filter(is_global_dish=True).order_by('-id')[:30]

class CustomProductCreateView(generics.CreateAPIView):
    serializer_class = CustomProductSerializer
    permission_classes =[permissions.IsAuthenticated]

    @extend_schema(
        summary="Создать блюдо (в т.ч. в глобальную базу)",
        description="Создает продукт. Если is_global_dish=true, он станет доступен всем для поиска."
    )
    def perform_create(self, serializer):
        serializer.save(creator=self.request.user)


class FoodLogDetailView(generics.RetrieveDestroyAPIView):
    serializer_class = FoodLogSerializer
    permission_classes = [permissions.IsAuthenticated]

    @extend_schema(
        summary="Удалить запись из дневника",
        description="Удаляет съеденный продукт по его ID. Пользователь может удалять только свои записи."
    )
    def get_queryset(self):
        return FoodLog.objects.filter(user=self.request.user)


class WaterTrackerView(APIView):
    permission_classes = [permissions.IsAuthenticated]

    @extend_schema(
        summary="Получить выпитую воду",
        description="Возвращает количество воды за указанную дату (или за сегодня).",
        parameters=[
            OpenApiParameter('date', str, description='Дата YYYY-MM-DD. По умолчанию - сегодня.', required=False)]
    )
    def get(self, request):
        date_str = request.query_params.get('date') or timezone.localdate().isoformat()
        water, _ = DailyWater.objects.get_or_create(user=request.user, date=date_str)
        return Response(DailyWaterSerializer(water).data)

    @extend_schema(
        summary="Добавить выпитую воду (+ стакан)",
        description="ПРИБАВЛЯЕТ указанное количество мл к текущему значению.",
        request=DailyWaterSerializer,
    )
    def post(self, request):
        date_str = request.data.get('date') or timezone.localdate().isoformat()
        try:
            add_amount = int(request.data.get('amount', 0))
        except ValueError:
            return Response({"error": "amount должен быть числом"}, status=400)

        water, _ = DailyWater.objects.get_or_create(user=request.user, date=date_str)
        water.amount += add_amount
        water.save()

        return Response(DailyWaterSerializer(water).data)

    @extend_schema(
        summary="Изменить выпитую воду (жесткая установка)",
        description="ПЕРЕЗАПИСЫВАЕТ значение воды (для исправления ошибок).",
        request=DailyWaterSerializer,
    )
    def patch(self, request):
        date_str = request.data.get('date') or timezone.localdate().isoformat()
        try:
            exact_amount = int(request.data.get('amount', 0))
        except ValueError:
            return Response({"error": "amount должен быть числом"}, status=400)

        water, _ = DailyWater.objects.get_or_create(user=request.user, date=date_str)
        water.amount = exact_amount
        water.save()

        return Response(DailyWaterSerializer(water).data)

    @extend_schema(
        summary="Сбросить воду за день в 0",
        parameters=[OpenApiParameter('date', str, description='Дата YYYY-MM-DD.', required=False)]
    )
    def delete(self, request):
        date_str = request.query_params.get('date') or timezone.localdate().isoformat()
        DailyWater.objects.filter(user=request.user, date=date_str).update(amount=0)
        return Response({'message': f'Вода за {date_str} сброшена до 0 мл'}, status=200)
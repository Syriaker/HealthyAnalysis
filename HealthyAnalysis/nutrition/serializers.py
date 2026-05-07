from rest_framework import serializers
from .models import Product, FoodLog, DailyWater

class ProductSerializer(serializers.ModelSerializer):
    class Meta:
        model = Product
        fields =['id', 'barcode', 'name', 'calories', 'proteins', 'fats', 'carbs', 'image_url']

class CustomProductSerializer(serializers.ModelSerializer):
    class Meta:
        model = Product
        fields =['id', 'name', 'calories', 'proteins', 'fats', 'carbs', 'is_global_dish']

class DailyWaterSerializer(serializers.ModelSerializer):
    class Meta:
        model = DailyWater
        fields = ['date', 'amount']

class FoodLogSerializer(serializers.ModelSerializer):
    product = ProductSerializer(read_only=True)
    product_id = serializers.PrimaryKeyRelatedField(
        queryset=Product.objects.all(), source='product', write_only=True
    )

    class Meta:
        model = FoodLog
        fields =['id', 'product', 'product_id', 'weight', 'meal_type', 'created_at']
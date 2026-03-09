from rest_framework import serializers
from .models import Product, FoodLog

class ProductSerializer(serializers.ModelSerializer):
    class Meta:
        model = Product
        fields =['id', 'barcode', 'name', 'calories', 'proteins', 'fats', 'carbs', 'image_url']

class FoodLogSerializer(serializers.ModelSerializer):
    product = ProductSerializer(read_only=True)
    product_id = serializers.PrimaryKeyRelatedField(
        queryset=Product.objects.all(), source='product', write_only=True
    )

    class Meta:
        model = FoodLog
        fields =['id', 'product', 'product_id', 'weight', 'meal_type', 'created_at']
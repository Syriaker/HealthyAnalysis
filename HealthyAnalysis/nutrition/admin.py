from django.contrib import admin
from .models import Product, FoodLog, DailyWater

@admin.register(Product)
class ProductAdmin(admin.ModelAdmin):
    list_display = ('name', 'barcode', 'calories', 'proteins', 'fats', 'carbs')
    search_fields = ('name', 'barcode')

@admin.register(FoodLog)
class FoodLogAdmin(admin.ModelAdmin):
    list_display = ('user', 'product', 'weight', 'meal_type', 'created_at')
    list_filter = ('meal_type', 'created_at')

@admin.register(DailyWater)
class DailyWaterAdmin(admin.ModelAdmin):
    list_display = ('user', 'date', 'amount')
    list_filter = ('date',)
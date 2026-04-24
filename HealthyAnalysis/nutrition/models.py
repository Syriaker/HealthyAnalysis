from django.db import models
from django.conf import settings


class Product(models.Model):
    barcode = models.CharField("Штрих-код", max_length=100, unique=True, null=True, blank=True, db_index=True)
    name = models.CharField("Название", max_length=255)

    calories = models.FloatField("Ккал (на 100г)", default=0)
    proteins = models.FloatField("Белки", default=0)
    fats = models.FloatField("Жиры", default=0)
    carbs = models.FloatField("Углеводы", default=0)

    image_url = models.URLField("Ссылка на картинку", blank=True, null=True)

    def __str__(self):
        return f"{self.name} ({self.barcode or 'Ручной ввод'})"


class FoodLog(models.Model):
    MEAL_CHOICES = (
        ('breakfast', 'Завтрак'),
        ('lunch', 'Обед'),
        ('dinner', 'Ужин'),
        ('snack', 'Перекус'),
    )

    user = models.ForeignKey(settings.AUTH_USER_MODEL, on_delete=models.CASCADE, related_name='food_logs')
    product = models.ForeignKey(Product, on_delete=models.CASCADE)
    weight = models.PositiveIntegerField("Вес съеденного (грамм)", default=100)
    meal_type = models.CharField("Прием пищи", max_length=20, choices=MEAL_CHOICES)

    created_at = models.DateTimeField("Дата и время добавления", auto_now_add=True)

    def __str__(self):
        return f"{self.user.email} съел {self.product.name} ({self.weight}г)"
from django.db import models
from django.conf import settings

class Biomarker(models.Model):
    name = models.CharField("Название", max_length=100)
    unit = models.CharField("Единица измерения", max_length=20)
    description = models.TextField("Описание", blank=True)

    def __str__(self):
        return f"{self.name} ({self.unit})"

class ReferenceRange(models.Model):
    GENDER_CHOICES = (
        ('M', 'Мужской'),
        ('F', 'Женский'),
        ('A', 'Любой'),  #пол не важен
    )

    biomarker = models.ForeignKey(Biomarker, on_delete=models.CASCADE, related_name='references')
    gender = models.CharField("Пол", max_length=1, choices=GENDER_CHOICES, default='A')
    min_age = models.PositiveIntegerField("Возраст от", default=0)
    max_age = models.PositiveIntegerField("Возраст до", default=100)

    min_value = models.FloatField("Нижняя граница нормы")
    max_value = models.FloatField("Верхняя граница нормы")

    def __str__(self):
        return f"{self.biomarker.name} ({self.gender} {self.min_age}-{self.max_age}): {self.min_value}-{self.max_value}"

class AnalysisRecord(models.Model):
    user = models.ForeignKey(settings.AUTH_USER_MODEL, on_delete=models.CASCADE, related_name='analysis_records')
    date = models.DateField("Дата сдачи")
    laboratory = models.CharField("Лаборатория", max_length=100, blank=True)
    comment = models.TextField("Комментарий", blank=True)
    created_at = models.DateTimeField(auto_now_add=True)

    def __str__(self):
        return f"Анализ от {self.date} ({self.user.email})"

class AnalysisResult(models.Model):
    STATUS_CHOICES = (
        ('low', 'Ниже нормы'),
        ('norm', 'Норма'),
        ('high', 'Выше нормы'),
        ('unknown', 'Нет нормы'),
    )

    record = models.ForeignKey(AnalysisRecord, on_delete=models.CASCADE, related_name='results')
    biomarker = models.ForeignKey(Biomarker, on_delete=models.PROTECT)
    value = models.FloatField("Значение")

    status = models.CharField("Статус", max_length=10, choices=STATUS_CHOICES, default='unknown', blank=True)

    def save(self, *args, **kwargs):
        try:
            profile = self.record.user.profile
        except:
            self.status = 'unknown'
            super().save(*args, **kwargs)
            return

        user_gender = profile.gender
        user_age = profile.age

        if user_age is None:
            self.status = 'unknown'
            super().save(*args, **kwargs)
            return
        ref = ReferenceRange.objects.filter(
            biomarker=self.biomarker,
            min_age__lte=user_age,
            max_age__gte=user_age
        ).filter(
            models.Q(gender=user_gender) | models.Q(gender='A')
        ).first()

        if ref:
            if self.value < ref.min_value:
                self.status = 'low'
            elif self.value > ref.max_value:
                self.status = 'high'
            else:
                self.status = 'norm'
        else:
            self.status = 'unknown'

        super().save(*args, **kwargs)

    def __str__(self):
        return f"{self.biomarker.name}: {self.value} ({self.status})"
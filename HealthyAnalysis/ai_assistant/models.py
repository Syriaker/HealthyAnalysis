from django.db import models
from django.conf import settings

class AIAdviceLog(models.Model):
    STATUS_CHOICES = (
        ('processing', 'Генерируется...'),
        ('ready', 'Готово'),
        ('error', 'Ошибка'),
    )

    user = models.ForeignKey(settings.AUTH_USER_MODEL, on_delete=models.CASCADE, related_name='ai_advices')
    advice_text = models.TextField("Текст совета от ИИ", blank=True, null=True)
    status = models.CharField("Статус", max_length=20, choices=STATUS_CHOICES, default='processing')
    created_at = models.DateTimeField("Дата генерации", auto_now_add=True)

    class Meta:
        verbose_name = "Совет ИИ"
        verbose_name_plural = "Советы ИИ"
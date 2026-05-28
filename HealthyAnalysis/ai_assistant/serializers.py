from rest_framework import serializers
from .models import AIAdviceLog

class AIAdviceLogSerializer(serializers.ModelSerializer):
    class Meta:
        model = AIAdviceLog
        fields = ['id', 'advice_text', 'created_at']
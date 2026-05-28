from django.contrib import admin
from .models import AIAdviceLog

@admin.register(AIAdviceLog)
class AIAdviceLogAdmin(admin.ModelAdmin):
    list_display = ('user', 'created_at')
    readonly_fields = ('advice_text',)
from django.contrib import admin
from .models import Biomarker, ReferenceRange, AnalysisRecord, AnalysisResult

class ReferenceRangeInline(admin.TabularInline):
    model = ReferenceRange
    extra = 1

@admin.register(Biomarker)
class BiomarkerAdmin(admin.ModelAdmin):
    list_display = ('name', 'unit')
    inlines = [ReferenceRangeInline]

class ResultInline(admin.TabularInline):
    model = AnalysisResult
    extra = 1
    readonly_fields = ('status',)

@admin.register(AnalysisRecord)
class AnalysisRecordAdmin(admin.ModelAdmin):
    list_display = ('user', 'date', 'laboratory')
    inlines = [ResultInline]
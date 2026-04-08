from rest_framework import serializers
from .models import AnalysisRecord, AnalysisResult, Biomarker


class AnalysisResultSerializer(serializers.ModelSerializer):
    biomarker_name = serializers.CharField(source='biomarker.name', read_only=True)
    unit = serializers.CharField(source='biomarker.unit', read_only=True)

    class Meta:
        model = AnalysisResult
        fields = ['biomarker', 'biomarker_name', 'value', 'status', 'unit']
        read_only_fields = ['status']


class AnalysisRecordSerializer(serializers.ModelSerializer):
    results = AnalysisResultSerializer(many=True)

    class Meta:
        model = AnalysisRecord
        fields = ['id', 'date', 'laboratory', 'comment', 'results']

    def create(self, validated_data):
        results_data = validated_data.pop('results')

        record = AnalysisRecord.objects.create(**validated_data)

        for res_data in results_data:
            AnalysisResult.objects.create(record=record, **res_data)

        return record
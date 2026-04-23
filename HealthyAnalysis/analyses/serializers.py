from rest_framework import serializers
from .models import AnalysisRecord, AnalysisResult, Biomarker
from django.utils import timezone

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

    def validate_date(self, value):

        if value > timezone.localdate():
            raise serializers.ValidationError("Дата анализа не может быть в будущем.")
        return value

    def create(self, validated_data):
        user = self.context['request'].user

        date = validated_data.get('date')
        laboratory = validated_data.get('laboratory', '')
        comment = validated_data.get('comment', '')
        results_data = validated_data.pop('results')

        record = AnalysisRecord.objects.filter(user=user, date=date).first()

        if record:
            if laboratory: record.laboratory = laboratory
            if comment: record.comment = comment
            record.save()
        else:
            record = AnalysisRecord.objects.create(
                user=user,
                date=date,
                laboratory=laboratory,
                comment=comment
            )

        for res_data in results_data:
            biomarker = res_data['biomarker']
            value = res_data['value']

            AnalysisResult.objects.update_or_create(
                record=record,
                biomarker=biomarker,
                defaults={'value': value}
            )

        return record
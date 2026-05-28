import requests
from django.utils import timezone
from datetime import timedelta
from rest_framework.views import APIView
from rest_framework.response import Response
from rest_framework import permissions
from drf_spectacular.utils import extend_schema
from profiles.models import UserProfile
from analyses.models import AnalysisResult
from nutrition.models import FoodLog, DailyWater


class AIGlobalAdviceView(APIView):
    permission_classes = [permissions.IsAuthenticated]

    @extend_schema(
        summary="Получить глобальный AI-анализ здоровья",
        description="Собирает профиль, воду, питание за 3 дня и последние анализы. Отправляет в локальную LLM."
    )
    def get(self, request):
        user = request.user

        try:
            profile = user.profile
            age = profile.age or "Не указан"
            gender = "Мужчина" if profile.gender == 'M' else "Женщина"
            height = profile.height or 0
            weight = profile.weight or 0
            bmi = round(weight / ((height / 100) ** 2), 1) if height and weight else "Неизвестно"
        except:
            return Response({"error": "Заполните профиль"}, status=400)

        all_results = AnalysisResult.objects.filter(record__user=user).order_by('-record__date')
        latest_analyses = {}
        for res in all_results:
            if res.biomarker.name not in latest_analyses:
                latest_analyses[res.biomarker.name] = res

        analyses_text = "\n".join([
            f"- {name}: {r.value} {r.biomarker.unit} (Статус: {r.get_status_display()})"
            for name, r in latest_analyses.items()
        ]) if latest_analyses else "Анализы не сдавались."

        three_days_ago = timezone.localdate() - timedelta(days=3)

        water_logs = DailyWater.objects.filter(user=user, date__gte=three_days_ago)
        avg_water = sum(w.amount for w in water_logs) / 3 if water_logs else 0

        food_logs = FoodLog.objects.filter(user=user, date__gte=three_days_ago)
        total_kcal, total_p, total_f, total_c = 0, 0, 0, 0
        for log in food_logs:
            factor = log.weight / 100
            total_kcal += log.product.calories * factor
            total_p += log.product.proteins * factor
            total_f += log.product.fats * factor
            total_c += log.product.carbs * factor

        avg_kcal = round(total_kcal / 3)
        avg_p = round(total_p / 3)
        avg_f = round(total_f / 3)
        avg_c = round(total_c / 3)

        prompt = f"""
        Ты профессиональный врач-диетолог и фитнес-тренер. Проанализируй данные пользователя и дай подробные рекомендации.

        ДАННЫЕ ПОЛЬЗОВАТЕЛЯ:
        - Пол: {gender}, Возраст: {age} лет.
        - Рост: {height} см, Вес: {weight} кг (ИМТ: {bmi}).

        ПИТАНИЕ И ВОДА (В среднем за последние 3 дня):
        - Вода: {int(avg_water)} мл/день.
        - Калории: {avg_kcal} ккал/день.
        - Макронутриенты: Белки {avg_p}г, Жиры {avg_f}г, Углеводы {avg_c}г.

        ПОСЛЕДНИЕ АНАЛИЗЫ КРОВИ:
        {analyses_text}

        ЗАДАЧА:
        Напиши структурированный ответ на русском языке. Используй форматирование Markdown.
        Обязательно включи следующие разделы:
        1. Оценка текущего состояния (ИМТ, питание, вода).
        2. Разбор анализов (если есть отклонения, объясни, что они значат).
        3. Конкретные рекомендации по питанию (какие продукты добавить, какие убрать).
        4. Рекомендации по образу жизни.

        Не используй общие фразы. Опирайся только на предоставленные цифры.
        """
        try:
            response = requests.post(
                'http://localhost:11434/api/generate',
                json={
                    "model": "qwen2.5",
                    "prompt": prompt,
                    "stream": False
                },
                timeout=60  
            )

            if response.status_code == 200:
                ai_answer = response.json().get('response', '')
                return Response({"advice": ai_answer})
            else:
                return Response({"error": "LLM вернула ошибку"}, status=502)

        except requests.exceptions.RequestException:
            return Response({"error": "Отказ AI. Убедись, что Ollama запущена на сервере!"}, status=503)
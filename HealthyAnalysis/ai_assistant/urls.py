from django.urls import path
from .views import AIGlobalAdviceView

urlpatterns = [
    path('advice/', AIGlobalAdviceView.as_view(), name='ai-advice'),
]
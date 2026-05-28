from django.urls import path
from .views import AIGlobalAdviceView, AICheckStatusView, AIAdviceHistoryView

urlpatterns = [
    path('advice/generate/', AIGlobalAdviceView.as_view(), name='ai-generate'),
    path('advice/status/<int:task_id>/', AICheckStatusView.as_view(), name='ai-status'),
    path('history/', AIAdviceHistoryView.as_view(), name='ai-history'),
]
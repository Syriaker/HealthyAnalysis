from django.urls import path
from .views import PersonalNormsView, AnalysisRecordView, LatestAnalysesView

urlpatterns = [
    path('norms/', PersonalNormsView.as_view(), name='personal-norms'),
    path('records/', AnalysisRecordView.as_view(), name='analysis-records'),
    path('latest/', LatestAnalysesView.as_view(), name='latest-analyses'),
]
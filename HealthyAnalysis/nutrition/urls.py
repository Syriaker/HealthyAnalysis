from django.urls import path
from .views import ScanProductView, FoodLogView

urlpatterns =[
    path('scan/', ScanProductView.as_view(), name='scan-barcode'),
    path('log/', FoodLogView.as_view(), name='log-food'),
]
from django.urls import path
from .views import ScanProductView, FoodLogCreateView

urlpatterns =[
    path('scan/', ScanProductView.as_view(), name='scan-barcode'),
    path('log/', FoodLogCreateView.as_view(), name='log-food'),
]
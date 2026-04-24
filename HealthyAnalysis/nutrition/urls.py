from django.urls import path
from .views import ScanProductView, FoodLogView, CustomProductCreateView

urlpatterns =[
    path('scan/', ScanProductView.as_view(), name='scan-barcode'),
    path('log/', FoodLogView.as_view(), name='log-food'),
    path('products/custom/', CustomProductCreateView.as_view(), name='custom-product'),
]
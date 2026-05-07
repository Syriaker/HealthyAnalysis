from django.urls import path
from .views import ScanProductView, FoodLogView, CustomProductCreateView, FoodLogDetailView, WaterTrackerView,GlobalDishSearchView

urlpatterns =[
    path('scan/', ScanProductView.as_view(), name='scan-barcode'),
    path('log/', FoodLogView.as_view(), name='log-food'),
    path('products/custom/', CustomProductCreateView.as_view(), name='custom-product'),
    path('log/<int:pk>/', FoodLogDetailView.as_view(), name='log-detail'),
    path('water/', WaterTrackerView.as_view(), name='water-tracker'),
    path('products/custom/', CustomProductCreateView.as_view(), name='custom-product'),
    path('dishes/search/', GlobalDishSearchView.as_view(), name='search-dishes'),
]
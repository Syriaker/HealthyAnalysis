from rest_framework import serializers
from .models import UserProfile

class UserProfileSerializer(serializers.ModelSerializer):
    email = serializers.EmailField(source='user.email', read_only=True)
    age = serializers.IntegerField(read_only=True)
    class Meta:
        model = UserProfile
        fields = ['email', 'birth_date', 'gender', 'height', 'weight', 'age']
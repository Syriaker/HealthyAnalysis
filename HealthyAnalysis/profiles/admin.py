from django.contrib import admin
from .models import UserProfile


@admin.register(UserProfile)
class UserProfileAdmin(admin.ModelAdmin):
    list_display = ('user', 'birth_date', 'age', 'gender', 'height', 'weight')

    list_filter = ('gender',)

    search_fields = ('user__email',)

    def age(self, obj):
        return obj.age
    age.short_description = 'Возраст'
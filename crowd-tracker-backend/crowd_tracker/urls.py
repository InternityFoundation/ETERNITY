from django.urls import path

from .views import station, best_stations

app_name = 'crowd_tracker'

urlpatterns = [
	path('station', station, name='station'),
	path('beststations', best_stations, name='best_stations')
]
from commons.utils.response import OK, Created

from django.views.decorators.http import require_http_methods

from .models import Station

import googlemaps


@require_http_methods(['GET'])
def station(request):
	result = Station.objects.get_all(projection={'_id':0})

	response = {
		'data': result
	} 
	return OK(response)


@require_http_methods(['POST'])
def best_stations(request):


	result = Station.objects.aggregate({
			'$sort': {
				'name': 1
			}
		})

	result = list(result)

	result = result[:3]

	response = {
		'data' : result
	}

	return Created(response)
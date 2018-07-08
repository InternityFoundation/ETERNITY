from commons.utils.response import OK, Created, NoContent

from django.views.decorators.http import require_http_methods

from .models import Station, CTRickshaw
import json
from commons.utils.http_error import BadRequest

from datetime import datetime, timedelta

from bson.objectid import ObjectId

import googlemaps

import pytz

import pickle
from operator import itemgetter, attrgetter

gmaps = googlemaps.Client(key='AIzaSyCR3sNkOzWlZmahHJxTQX1pRsHJjZyEOQ0')

pickle_in = open("crowd_tracker/metro_data", 'rb')
arr = pickle.load(pickle_in)

sto = open('crowd_tracker/station_code', 'rb')
st_cd1 = pickle.load(sto)

# print(st_cd1)


def weekbefore(dt,st_cd, arr):
    wd = dt - timedelta(weeks=1)
    try:
        y = wd.year
        m = wd.month
        d = wd.day
        h = wd.hour
        return arr[st_cd][y-2015][m-1][d-1][h]
    except:
        return None


def monthbefore(dt,st_cd, arr):
    wd = dt - timedelta(days=30)
    try:

        y = wd.year
        m = wd.month
        d = wd.day
        h = wd.hour
        print(y,m,d,h)
        return arr[st_cd][y-2015][m-1][d-1][h]
    except:
        return None

def yearbefore(dt,st_cd, arr):
    wd = dt - timedelta(days=375)
    try:
        y = wd.year
        m = wd.month
        d = wd.day
        h = wd.hour
        return arr[st_cd][y-2015][m-1][d-1][h]
    except:
        return None

def twoyearbefore(dt,st_cd, arr):
    wd = dt - timedelta(days=750)
    try:
        y = wd.year
        m = wd.month
        d = wd.day
        h = wd.hour
        return arr[st_cd][y-2015][m-1][d-1][h]
    except:
        return None



@require_http_methods(['GET'])
def station(request):
    result = Station.objects.get_all(projection={'_id':0})

    response = {
        'data': result
    } 
    return OK(response)


def func(a,b):
    print(a,b)

@require_http_methods(['POST'])
def book_rickshaw(request):

    data = json.loads(request.body.decode('utf8'))
    x = data.get('lat',0)
    y = data.get('lng',0)
    destination = data.get('stationCode',None)

    if not destination or not x or not y:
        raise BadRequest


    
    result = CTRickshaw.objects.get_one(queries={
        'location': {
            '$near' : {
                '$geometry': {
                    'type': 'Point',
                    'coordinates': [y,x]
                },
                '$maxDistance': 200000000
            }
        },
        'capacity' : {'$lt':4},
        'destination': destination

    })

    resul = CTRickshaw.objects.update_one(queries={
        'location': {
            '$near' : {
                '$geometry': {
                    'type': 'Point',
                    'coordinates': [y,x]
                },
                '$maxDistance': 200000000
            }
        },
        'capacity' : {'$lt':4},
        'destination': destination

    },
    data = {
        '$inc' : {'capacity':1}
    })



    print(result)


    if result:
        result['capacity'] += 1
        return OK({'data':result})
    else:
        return NoContent()



@require_http_methods(['POST'])
def best_stations(request):

    data = json.loads(request.body.decode('utf8'))
    x = data.get('lat',0)
    y = data.get('lng',0)

    
    result = Station.objects.get_all(queries={
        'location': {
            '$near' : {
                '$geometry': {
                    'type': 'Point',
                    'coordinates': [y,x]
                },
                '$maxDistance': 200000000
            }
        }
    })

    result = list(result[:3])

    d = datetime.now()
    # print(d)
    # print(result['stationCode'])

    

    for r in result:

        week_before = weekbefore(d, st_cd1[r['stationCode']] ,arr)
        month_before = monthbefore(d, st_cd1[r['stationCode']] ,arr)
        year_before = yearbefore(d, st_cd1[r['stationCode']] ,arr)
        two_year_before = twoyearbefore(d, st_cd1[r['stationCode']] ,arr)

        directions_result = gmaps.directions({'lat':r['location'][1],'lng':r['location'][0]},
                                     {'lat':x,'lng':y},
                                     mode="driving",
                                     departure_time=d
                                    )

        r['eta'] = directions_result[0]['legs'][0]['duration']['text']

        total = 0
        cnt = 0

        if week_before:
            total += week_before*8
            cnt += 8

        if month_before:
            total += month_before*4
            cnt += 4

        if year_before:
            total += year_before*2
            cnt += 2

        if two_year_before:
            total+= two_year_before*1
            cnt += 1

        if cnt!=0:
            r['population'] = int(total/cnt)
        else:
            r['population'] = 0



    

    response = {
        'data' : sorted(result, key=itemgetter('population'))
    }

    return OK(response)
from json import loads
from datetime import datetime
from bson.objectid import ObjectId
from django.core.serializers.json import DjangoJSONEncoder


class CustomJsonEncoder(DjangoJSONEncoder):
    '''A custom JSON encoder handling JSON native encoding for following python types
        - datetime
        - bson object id
    '''

    def default(self, obj):
        '''JSON Encoding handler method
        '''

        if isinstance(obj, ObjectId):
            return str(obj)

        if isinstance(obj, datetime):
            return int(float(obj.strftime('%s')) * 1000)

        return super(CustomJsonEncoder, self).default(obj)


def is_json(json_string):
    '''Evaluates if object in parameter is json serializable

    Args:
        json_string: string object to be evaluated

    Returns:
        A tuple having boolean representing if sent object is JSON serializable with deserialized dictionary if True
        eg:
            (True, {'name': 'John Doe'})
    '''

    try:
        return True, loads(json_string)

    except ValueError:
        return False, json_string

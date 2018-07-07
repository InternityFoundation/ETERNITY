import traceback
import ujson

import requests
from django.conf import settings

from .loggers import app_logger


def make_request(url, method, params=None, headers=None, data=None, json=None, timeout=None):
    '''Make external request to a URL using python's request module.

    Args:
        url: URL of the request.
        method: method of the request.
        params: (optional) Dictionary or bytes to be sent in the query string.
        headers: (optional) Dictionary of HTTP Headers to send.
        data: (optional) Dictionary or list of tuples, bytes, or file-like object to send in the body.
        json: (optional) A JSON serializable Python object to send in the body.
        timeout: (optional) How many seconds to wait for the server to send data.

    Returns:
        A tuple containing response of the request in JSON format, binary format and HTTP code of the response and
        message of the error in making request (if any).
    '''

    response_json = {}
    response_content = None
    response_code = None
    error = None

    req = {}

    if headers:
        req.update({'headers': headers})

    if params:
        req.update({'params': params})

    if data:
        req.update({'data': data})

    if json:
        req.update({'json': json})

    if timeout:
        req.update({'timeout': timeout})

    request_log = '{url} :: {method} :: {request}'.format(
        url=url,
        method=method,
        request=ujson.dumps(req)
    )
    app_logger.info(request_log)

    try:
        response = requests.request(method, url, **req)
        response_content = response.content
        response_code = response.status_code
        response_log = '{code} :: {content} \n#------------------------------------------#\n'.format(
            code=response_code,
            content=(response_content if (int(response_code/100) != 2 or settings.DEBUG) else b'{}').decode('utf-8')
        )
        app_logger.info(response_log)
        response_json = response.json()

    except ValueError:
        error = traceback.format_exc()
        log = '{error}\n#------------------------------------------#\n'.format(error=error)
        app_logger.error(log)
        pass

    except Exception as e:
        raise

    return (response_json, response_content, response_code, error)

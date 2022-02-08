import os

REST_END_POINT = os.getenv("REST_END_POINT", default="")
UPLOAD_URL = REST_END_POINT + "/files"
CONVERSION_URL = REST_END_POINT +"/file-conversion"

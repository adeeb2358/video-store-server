import magic
import requests

import config
from file_utils import FileUtils
from response_parser import ResponseParser


class ConvertRequest:
    def __init__(self):
        self.CONVERSION_URL = config.CONVERSION_URL
        self.mime = magic.Magic(mime=True)

    def list(self):
        response = requests.session().get(self.CONVERSION_URL)
        ResponseParser().print_table(response.text)

    def download(self, fileid):
        response = requests.session().get(self.CONVERSION_URL + "/" + fileid)
        if response.status_code != 200:
            print("Status:" +str( response.status_code))
            print("Message:" + response.text)
            return
        FileUtils().save_file(response)

    def convert(self, fileid):
        response = requests.session().post(self.CONVERSION_URL + "/" + fileid)
        print("Status:" + str( response.status_code))
        print("Message:" + response.text)

    def reconvert(self, fileid):
        response = requests.session().put(self.CONVERSION_URL + "/" + fileid)
        print("Status:" + str( response.status_code))
        print("Message:" + response.text)

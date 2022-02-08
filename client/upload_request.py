import os.path

import magic
import requests
from requests_toolbelt.multipart.encoder import MultipartEncoder
from response_parser import ResponseParser

import config
from file_utils import FileUtils


class UploadRequest:
    def __init__(self):
        self.UPLOAD_URL = config.UPLOAD_URL
        self.mime = magic.Magic(mime=True)

    def post(self, file_path):
        if not file_path.is_file():
            print("It is not a file")
            return

        file_name = os.path.basename(file_path)
        file_mime = self.mime.from_file(str(file_path))
        file = open(file_path, 'rb')
        multipart_file = MultipartEncoder(
            fields={'file': (file_name, file, file_mime)})
        headers = {'Content-Type': multipart_file.content_type}
        response = requests.session().post(self.UPLOAD_URL,
                                           data=multipart_file,
                                           headers=headers
                                           )
        self.print_response(response)
        pass

    def delete(self, file_id):
        response = requests.session().delete(self.UPLOAD_URL + '/' + file_id)
        self.print_response(response)
        pass

    def download(self, file_id):
        response = requests.session().get(self.UPLOAD_URL + '/' + file_id,
                                          stream=True)
        print('ResponseStatusCode:' + str(response.status_code))
        if response.status_code == 200:
            FileUtils().save_file(response)
        else:
            print(response.text)

    def list(self):
        response = requests.session().get(self.UPLOAD_URL)
        ResponseParser().print_table(response.text)

    def print_response(self, response):
        print('ResponseStatusCode:' + str(response.status_code))
        if response.text == '':
            print('Operation successfully completed')
        else:
            print(response.text)

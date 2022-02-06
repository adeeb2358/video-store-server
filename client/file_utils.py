import pathlib


class FileUtils:
    def __init__(self):
        pass

    def get_file_name(self, response):
        return response.headers['Content-Disposition'].split("=")[1][1:-1]

    def get_save_file_name(self, file_name):
        save_file_name = file_name
        if pathlib.Path(file_name).exists():
            print(file_name + " file_already_exists. Saving to new file")
            num = 1;
            while pathlib.Path(save_file_name).exists():
                file_parts = save_file_name.split(".")
                name = file_parts[0] + '(' + str(num) + ')'
                save_file_name = name
                num = num + 1
                print(len(file_parts))
                if len(file_parts) == 2:
                    save_file_name = save_file_name + '.' + file_parts[1]
        return save_file_name

    def save_file(self, response):
        file_name = self.get_file_name(response)
        save_file_name = self.get_save_file_name(file_name)
        file = open(save_file_name, "wb")
        for chunk in response.iter_content(chunk_size=1024):
            file.write(chunk)
        print(save_file_name + " Saved Successfully")
        return

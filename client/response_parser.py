import ast
import json

import pandas as pd


class ResponseParser:
    def __init__(self):
        pass

    def print_table(self, json_content):
        ast_literal = ast.literal_eval(json_content)
        loaded_json = json.loads(json.dumps(ast_literal))
        if len(loaded_json) > 0:
            dictionary = loaded_json[0]
            tags = dictionary.keys()
            data = []
            for json_content in loaded_json:
                data.append(json_content.values())
            print(pd.DataFrame(data, columns=tags))
            return
        print("No Contents to List")

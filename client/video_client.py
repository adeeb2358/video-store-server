from pathlib import Path

import typer

from rest_request import RestRequest

app = typer.Typer()


@app.command(help="list ")
def list():
    RestRequest().list()


@app.command(help="upload <path-to-file>")
def upload(filepath: Path):
    RestRequest().post(filepath)


@app.command(help="delete <file-id>")
def delete(file_id: str):
    RestRequest().delete(file_id)


@app.command(help="download <file-id>")
def download(file_id: str):
    RestRequest().download(file_id)


if __name__ == "__main__":
    app()

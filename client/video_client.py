from pathlib import Path

import typer

from convert_request import ConvertRequest
from upload_request import UploadRequest

app = typer.Typer()


@app.command(help="list ")
def list():
    UploadRequest().list()


@app.command(help="upload <path-to-file>")
def upload(filepath: Path):
    UploadRequest().post(filepath)


@app.command(help="delete <file-id>")
def delete(file_id: str):
    UploadRequest().delete(file_id)


@app.command(help="download <file-id>")
def download(file_id: str):
    UploadRequest().download(file_id)


@app.command(help="convert <file-id>")
def convert(file_id: str):
    ConvertRequest().convert(file_id)


@app.command(help="reconvert <file-id>")
def reconvert(file_id: str):
    ConvertRequest().reconvert(file_id)


@app.command(help="downloadconverted <file-id>")
def downloadconverted(file_id: str):
    ConvertRequest().download(file_id)


@app.command(help="convertlist")
def convertlist():
    ConvertRequest().list()


if __name__ == "__main__":
    app()

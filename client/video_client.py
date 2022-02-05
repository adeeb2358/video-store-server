from pathlib import Path

import typer

from rest_request import RestRequest

app = typer.Typer()


@app.command()
def upload(filepath: Path):
    RestRequest().post(filepath)


@app.command()
def delete(file_id: str):
    RestRequest().delete(file_id)


@app.command()
def download(file_id: str):
    RestRequest().download(file_id)


@app.command()
def list():
    print("list called")


if __name__ == "__main__":
    app()

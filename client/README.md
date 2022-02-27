### Video Storage Server Client

> > A basic python client designed to do CRUD operation using the video server.

> Operations Supported by the client

1. Upload  (Upload files to the server)
2. Download  (Download file from the server)
3. List (List all uploaded files)
4. Delete (Delete one uploaded file)
5. downloadconverted (download converted file from the server)
6. convertlist (list the converted file details)
7. reconvert (convert the failed downloads)
8. convert(convert the uploaded video file into webm format)

> Note: For help execute the following command.
> sh video-store.sh --help

### PreRequisites

1. Install python 3.9 or greater
2. pip 21.1.3 or greater
3. Dependencies specified on [requirements.txt](requirements.txt) file
1. Gnu bash 3.2.57 or greater

### Installation

1. Run
   > sh setup.sh
2. Running tests
   > sh tests.sh

### Running the client

Run
> sh video-store.sh  <action> <file_param>

Note: REST End Point can be changed at video-store.sh


> [Click here]() to Video Storage REST API Docs
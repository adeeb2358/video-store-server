## Woven Video Store Back End REST API

This is a basic video storage API which has the following functionalities.

> #### REST End Points
>
>Version 1.0
>
>1. Upload video files to the server
    >   > POST /v1/files
>2. Download Video files
    >   > GET /v1/files/{fileid}
>3. Delete uploaded files.
    >   > DELETE /v1/files/{fileid}
>4. List uploaded video files.
    >   > GET /v1/files

Note: The application only accepts the following media format.

1. video/mpeg
2. video/mp4

Additional Notes:

1. Uploaded Videos saved on the video-store/ directory
2. Maximum File Upload Size : 5gb
3. This version of the API is unauthorized
4. List API not paginated.

### New Features V2.0

Asynchronous Video Conversion Service

1. Convert mp4 to webm
2. Convert mpeg to webm

> 1. Convert video files in the server
     >   > POST /v1/file-conversion/{fileid}
>2. Download Converted Video files
    >   > GET /v1/file-conversion/{fileid}
>3. Reconvert failed conversion files.
    >   > PUT /v1/files/{fileid}
>4. List uploaded video files.
    >   > GET /v1/files

### Components in the application

> 1. Conversion Adapter built on top of Jave2 library
> 2. Asynchronous Web service using thread executor
     > Note:- Since the scope of this application is limited(batch jobs are note used for video conversion)
> 3. Configurable thread pool paramters are defined.
> 4. Mongo db for storing all details of the converted and non converted files which are uploaded on the server

### API Spec

1. Click [v1.0](specs/open-api-v1.0.yaml) to view Open API Specification YAML File
2. Click [v2.0](specs/open-api-v2.0.yaml) to view open API Specification YAML File
3. After deployment Click
   [swagger-url](http://localhost:8080/swagger-ui/index.html)
   or [open-api](http://localhost:8080/v3/api-docs/) for api spec visualization.

## Building

### PreRequisites

Install these on the build machine

1. Open JDK15
2. gradle 6.7.1 or greater
3. Docker 20.10.6 or greater

### Build

Build the application with all test cases
> sh ./build.sh

Note: build process may take half an hour to download all the dependency for ffmpeg
(This is for first time build only. Subsequent build will be faster)
Additional build time of nearly 5 minutes for each video conversion tasks.

## Deploy

Run the application
> sh ./run.sh

### Client

Click [here](client/README.md) to view the complete documentation of the client application.

## Technologies Used

1. Spring Boot Framework 2.6.3
2. MongoDB (Latest will be downloaded from docker repo)
3. MongoDB Test container used for running tests
4. FFMEG Jave2 Wrapper for all video conversion

> Mongo Express(this can be removed from the docker-compose) This is to manage the mongo db.
[Click here after deployment](http://localhost:8081)

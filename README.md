## Woven Video Store Back End REST API

This is a basic video storage API which has the following functionalities.

>#### REST End Points
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
>   > GET /v1/files/{fileid}

Note: The application only accepts the following media format.
1. video/mpeg
2. video/mp4

Additional Notes:

1. Uploaded Videos saved on the video-store/ directory
2. Maximum File Upload Size : 5gb
3. This version of the API is unauthorized
4. List API not paginated.

### API Spec

1. Click [here](specs/open-api-modified.yaml) to view Open API Specification YAML File
2. After deployment Click
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

## Deploy

Run the application
> sh ./run.sh

### Client
Click [here](client/README.md) to view the complete documentation of the client application.

## Technologies Used

1. Spring Boot Framework 2.6.3
2. MongoDB (Latest will be downloaded from docker repo)
3. MongoDB Test container used for running tests 

> Mongo Express(this can be removed from the docker-compose) This is to manage the mongo db.
[Click here after deployment](http://localhost:8081)

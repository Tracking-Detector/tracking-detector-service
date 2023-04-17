# Tracking Detector Service

This service is the backend component which manages all the training and data set creation of the request data. It stores request data and generates csv exports and starts the training of the models via XMLRPC.
It is written in Kotlin with Springboot 3 and implements a job architecture inspired by [OTTO-DE/edison-microservice](https://github.com/otto-de/edison-microservice).

## Requirements
This service is designed to run within a docker compose application. By default, it runs on port 3000. It requires a minio instance and a XMLRpc training service as well as the frontend.
It can be pulled from this dockerhub repository: [Tracking-Detector-Service](https://hub.docker.com/repository/docker/heschwerdt/tracking-detector-service/general).

It exposes the following environment variables for you to configure:

### MongoDB

* MONGO_DB_HOST -> Hostname in the network for the mongodb connection
* MONGO_DB_PORT -> The mongoDB port
* MONGO_DB_DATABASE -> The database name

### XMLRPC

* RPC_HOST -> XMLRPC hostname
* RPC_PORT -> XMLRPC port
* RPC_METHOD -> XMLRPC methodname to call for training

### MINIO

* MINIO_URL -> Minio URL
* MINIO_ACCESS_KEY -> Minio Public Key
* MINIO_PRIVATE_KEY -> Minio Private Key
* TRAINING_DATA_BUCKET -> Bucket name for the exported training data
* MODEL_BUCKET -> Model bucket to serve the models from
* MONGO_DB_BACKUP_BUCKET -> Backup bucket to save the mongo data into

## Jobs

### RequestDataExportJob

The RequestDataExportJob takes a FeatureExtractor configuration and exports the requests data with the help of this extractor into a csv.gz file and stores it on the minio training data bucket. If you want to add a format for the data you can just throw together a new extractor and create a new JobConfiguration. Then the job will be added and can be executed. 

### ModelTrainingJob

The ModelTrainingJob loops over all the KerasModelDefinitions in the MongoDB and trains them calling a XMLRPC method on the python RPC Server. This RPC server then stores the trained model in the minio bucket for models.

### CleanUpJob

The CleanUpJob deletes old JobRuns so that the MongoDB is fresh and does not get flooded with a lot of job runs that not needed.

## Endpoint

| Endpoint                                                                                               | Method | Description                                                                                    |      
|--------------------------------------------------------------------------------------------------------|--------|------------------------------------------------------------------------------------------------|
| [/tracking-detector/models/](/tracking-detector/models/)                                               | GET    | Returns all available KerasModel with their training runs and their achieved accuracy          |
| [/tracking-detector/models/](/tracking-detector/models/)                                               | POST   | Creates a new KerasModel to train from the exported data                                       |                                                                                       
| [/tracking-detector/models/{id}](/tracking-detector/models/{id})                                       | GET    | Returns a specific KerasModel and its training runs.                                           |
| [/tracking-detector/models/{id}](/tracking-detector/models/{id})                                       | PUT    | Updates a specific KerasModel.                                                                 |
| [/tracking-detector/files/](/tracking-detector/files)                                                  | GET    | Returns a list of all available models to download.                                            |
| [/tracking-detector/files/models/{folder}/{object}](/tracking-detector/files/models/{folder}/{object}) | GET    | Downloads a specific resource for the models which can be used in the tensorflowjs load Model. |
| [/tracking-detector/requests](/tracking-detector/requests)                                             | POST   | Creates a new RequestDataObject in the database which can be used for the export.              |
| [/tracking-detector/jobs/](/tracking-detector/jobs/)                                                   | GET    | Returns a list of available jobs and some meta information about that job.                     |
| [/tracking-detector/jobs/{id}](/tracking-detector/jobs/{id})                                           | GET    | Returns a specific job by id.                                                                  |
| [/tracking-detector/jobs/{id}/isActive](/tracking-detector/jobs/{id}/isActive)                         | GET    | Returns weather a specific job is active or not.                                               |
| [/tracking-detector/jobs/{id}/runs](/tracking-detector/{id}/runs)                                      | GET    | Returns all the job runs for a specific job.                                                   |
| [/tracking-detector/jobs/{id}/runs](/tracking-detector/{id}/toggle)                                    | PATCH  | Toggles the enabled status of the job.                                                         |
| [/tracking-detector/jobs/{id}/runs](/tracking-detector/{id}/trigger)                                   | POST   | Triggers a job to run immediately.                                                             |
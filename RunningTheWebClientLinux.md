This document describes how to run the sputnik web client on a linux machine using the google app engine dev server.  Running the app takes three steps:

  1. Install the prerequisites, including their respective prerequisites.
  1. Run the app on your local machine.
  1. Load test data into the app's datastore.

## Prerequisites ##

  * [Google App Engine](http://code.google.com/appengine/downloads.html) python SDK
  * The [sputnik source code](http://code.google.com/p/sputniktests/source/checkout)

## Launching the app ##

This step launches an instance of sputnik with an empty datastore.

  1. Enter the directory where you checked out sputnik
  1. Create a symlink for `app` called `glabs20-sputnik`
```
    ln -s app glabs20-sputnik
```
  1. Run the app on the app engine dev appserver
```
    <path-to-app-engine>/dev_appserver.py glabs20-sputnik --datastore_path sputnik.datastore
```

The datastore option causes the app to use the same file for the test data on each invocation rather than a new clean datastore so you can reuse it across several runs of the app.  Clean the datastore by deleting this file and reloading the app.

The above command will give you an instance of the web app running on port 8080.  To run it on a different port use the `--port` option; in the following I'll assume the port is 8080.

If you want the instance to not just be accessible through `localhost:8080` pass `--address 0.0.0.0` as an argument.  Then the app will be accessible from other machines on your network through `<machine-name>:8080`.

## Loading test data ##

You load test data into the datastore using the app engine app config tool.  The datastore contains three different types of entities: test cases, test suites and test versions.  The app config tool only allows you to update one of these at a time so you have to do this in three steps.

To upload the test cases do this from the root directory of your sputnik checkout:

```
PYTHONPATH=$PYTHONPATH:glabs20-sputnik <path-to-app-engine>/appcfg.py upload_data \
  --config_file glabs20-sputnik/loader.py \
  --filename . \
  --loader_opts suite:1 \
  --url http://localhost:8080/remote_api \
  --kind Case \
  glabs20-sputnik
```

The same command is used for the two others but with `Suite` and `Version` instead of `Case`.

Once these three commands have completed you should be able to visit `http://localhost:8080` and see a fully functional instance of the sputnik web app.  You may notice that the images are missing; that's because they are not open source like the rest of the code and so are not available through the sputnik source repository.
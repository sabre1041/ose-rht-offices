# RHT Offices application for OpenShift 3

This sample application will create and deploy a JBoss EAP application server as well as a MongoDB database.  The sample application will display a map and perform geospatial queries to populate the map with all Red Hat Offices in North America


## Quick instructions to just get this working on an OpenShift 3 deployment as a normal user

````
$ oc login https://yourOpenShiftServer
$ oc new-project rhtoffices
$ oc create -f https://raw.githubusercontent.com/sabre1041/ose-rht-offices/master/rhtoffices-template.json
$ oc new-app rhtoffices
````
Once the application is deployed and running, you can also scale the number of EAP pods to 2 with the following commands:

````
$ oc scale --replicas=2 rc rhtoffices-1
````

## Install template as cluster-admin for everyone to use

Load the template with cluster-admin user:

````
# oc create -f https://raw.githubusercontent.com/sabre1041/ose-rht-offices/master/rhtoffices-template.json -n openshift
````

## Featured offices

By default, offices are indicated with a blue maker on the map. To highlight particular offices, a file at `src/main/resources/offices.properties` can be used to designate certain offices by modifying the `featuredOffice` property and using their office code as shown below

    featuredOffices=nyc

Multiple offices can be featured by specifying a comma separated list of office codes 

*Note*: Codes can be found by inspecting the `src/main/resources/rhtoffices.json` file


This project was based on the work by Grant Shipley and the [MLB Stadiums](https://github.com/gshipley/openshift3mlbparks) application for the [OpenShift RoadShow](http://training.runcloudrun.com/roadshow/).

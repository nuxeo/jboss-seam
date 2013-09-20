
Custom Seam build for Nuxeo based on version 2.1.0.SP1

## Nuxeo changes

Allow Nuxeo Runtime services injection in Seam beans : [NXP-12207](https://jira.nuxeo.com/browse/NXP-12207)

Better manage Seam conversation concurrency : [NXP-12487](https://jira.nuxeo.com/browse/NXP-12487)

## Build eclipse project 

    mvn -f build/core.pom.xml --settings build/ci.settings.xml eclipse:eclipse

## Seam Core

### Build

    ant build

### Deploy

    mvn org.apache.maven.plugins:maven-deploy-plugin:2.7:deploy-file \
        -Durl=https://maven-eu.nuxeo.org/nexus/content/repositories/vendor-snapshots \
        -DrepositoryId=vendor-snapshots \
        -Dfile=./lib/jboss-seam.jar \
        -DpomFile=./classes/poms/core.wls.pom \
        -Dsources=./lib/src/jboss-seam-sources.jar

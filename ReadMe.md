
Custom Seam build for Nuxeo based on version 2.1.0.SP1

## Nuxeo changes

Changes are done using the [VEND JIRA project](https://jira.nuxeo.com/browse/VEND) and component "Seam".
Nuxeo changes on this version are listed there for each released version, for instance:

- [2.1.0.SP1-NX1](https://jira.nuxeo.com/browse/VEND/fixforversion/16241)
- [2.1.0.SP1-NX2](https://jira.nuxeo.com/browse/VEND/fixforversion/16242)
- [2.1.0.SP1-NX3](https://jira.nuxeo.com/browse/VEND/fixforversion/16243)

## Build eclipse project 

    mvn -f build/core.pom.xml --settings build/ci.settings.xml eclipse:eclipse

## Seam Core

### Build

    ant build

### Deploy

    ./mvndep.sh [release]

### Change version

     vi build/core.pom.xml
     vi build/remoting.pom.xml
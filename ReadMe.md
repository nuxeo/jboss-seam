
Custom Seam build for Nuxeo based on version 2.1.0.SP1.

Usual Nuxeo maintenance branch for this version is on master, this
branch is used for fixes against 2.1.0.SP1, without further changes to
sync with Nuxeo code.

## Nuxeo changes

Changes are done using the [VEND JIRA project](https://jira.nuxeo.com/browse/VEND) and component "Seam".
Nuxeo changes on this version are listed there for each released version, for instance.

Versions on this branch will concern jboss-seam-remoting only.

## Seam Remoting

### Build

    ant build

### Deploy

    ./mvndep.sh [release]

### Change version

     vi build/remoting.pom.xml
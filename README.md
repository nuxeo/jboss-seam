# JBoss Seam

Fork for Nuxeo patches, see repository at https://github.com/nuxeo/jboss-seam/ on branch Nuxeo_2_3_1.

Continuous integration at http://qa.nuxeo.org/jenkins/job/jboss-seam-2.3.1/.
Issues and reports at https://jira.nuxeo.com/browse/VEND.

## Build

Only the jboss-seam module needs to be patched, currently.
It can be built running the following command from the jboss-seam subdirectory:

    $ mvn clean install -DskipTests=true

## Deploy

To deploy this module, the version should be changed in the jboss-seam/pom.xml version.
The usual maven deploy command can be used.
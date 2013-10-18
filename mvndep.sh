TARGET_REPO=https://maven-eu.nuxeo.org/nexus/content/repositories/vendor-snapshots
[ "$1" == "release" ] && TARGET_REPO=https://maven-eu.nuxeo.org/nexus/content/repositories/vendor-releases
mvn org.apache.maven.plugins:maven-deploy-plugin:2.7:deploy-file \
                   -Durl=$TARGET_REPO \
                   -DrepositoryId=vendor-snapshots \
                   -Dfile=./lib/jboss-seam.jar \
                   -DpomFile=./classes/poms/core.wls.pom \
                   -Dsources=./lib/src/jboss-seam-sources.jar

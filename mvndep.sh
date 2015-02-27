TARGET_REPO=https://maven-eu.nuxeo.org/nexus/content/repositories/vendor-snapshots
TARGET_REPO_ID=vendor-snapshots
[ "$1" == "release" ] && TARGET_REPO=https://maven-eu.nuxeo.org/nexus/content/repositories/vendor-releases
[ "$1" == "release" ] && TARGET_REPO_ID=vendor-releases
#mvn org.apache.maven.plugins:maven-deploy-plugin:2.7:deploy-file \
#                   -Durl=$TARGET_REPO \
#                   -DrepositoryId=$TARGET_REPO_ID \
#                   -Dfile=./lib/jboss-seam.jar \
#                   -DpomFile=./classes/poms/core.pom \
#                   -Dsources=./lib/src/jboss-seam-sources.jar
mvn org.apache.maven.plugins:maven-deploy-plugin:2.7:deploy-file \
                   -Durl=$TARGET_REPO \
                   -DrepositoryId=$TARGET_REPO_ID \
                   -Dfile=./lib/jboss-seam-remoting.jar \
                   -DpomFile=./classes/poms/remoting.pom \
                   -Dsources=./lib/src/jboss-seam-remoting-sources.jar

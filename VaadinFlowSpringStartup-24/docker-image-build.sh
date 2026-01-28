#!/bin/bash

AppVersion="v1.0"
DockerHubUser="<username>"                     #username@gmail.com
DockerHubRepoName="<repo-name>"
DockerHubRepository="${DockerHubUser}/${DockerHubRepoName}"
#
docker login --password <password> --username ${DockerHubUser}
###
Application="MyApp"
ApplicationDir="./VaadinFlowSpringStartup-24"  #Realtive to the current multi-module project structure.
echo "Creating ${Application} Image"
#mvn clean package -DskipTests -Pproduction
mvn -pl ${ApplicationDir} -am clean package -DskipTests -Pproduction
docker image build -f ${ApplicationDir}/Dockerfile -t ${Application}:${AppVersion} ${ApplicationDir}/
docker image tag ${Application}:${AppVersion} ${DockerHubRepository}:${Application}-${AppVersion}
docker push ${DockerHubRepository}:${Application}-${AppVersion}
#
###
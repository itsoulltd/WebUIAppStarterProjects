#FROM library/tomcat:8.5.35
FROM library/tomcat:9.0.40-jdk8-adoptopenjdk-openj9
MAINTAINER lab.infoworks.com

# Delete existing ROOT folder
RUN rm -rf /usr/local/tomcat/webapps/ROOT

# Now deploy web-application.war to ../tomcat/webapps
ADD target/ROOT.war /usr/local/tomcat/webapps/

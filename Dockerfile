FROM openjdk:latest

ENV HZ_HOME /opt/hazelcast/
RUN mkdir -p $HZ_HOME

# User root while setup
USER root

ADD hazelcast.xml /$HZ_HOME/hazelcast.xml
ADD start.sh /$HZ_HOME/start.sh

RUN chmod a+x /$HZ_HOME/start.sh
RUN chmod a+rw /$HZ_HOME/*

# Start hazelcast standalone server.
CMD /$HZ_HOME/start.sh
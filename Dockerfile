FROM ubuntu:18.04

#Java
RUN apt-get update && apt-get upgrade -y &&\
	apt-get install -y software-properties-common &&\
	add-apt-repository ppa:openjdk-r/ppa &&\
	apt-get -y install openjdk-8-jdk

ENV JAVA_HOME /usr/lib/jvm/java-8-openjdk-amd64
ENV PATH $JAVA_HOME/bin:$PATH

RUN apt-get update && apt install -y wget \
	curl

#Scala
RUN wget https://scala-lang.org/files/archive/scala-2.12.8.deb &&\
	dpkg -i scala-2.12.8.deb &&\
	apt-get update &&\
	apt-get install scala

#sbt
RUN curl -L -o sbt-1.4.8.deb http://dl.bintray.com/sbt/debian/sbt-1.4.8.deb &&\
	dpkg -i sbt-1.4.8.deb &&\
	rm sbt-1.4.8.deb &&\
	apt-get update &&\
	apt-get install sbt

#npm
RUN curl -fsSL https://rpm.nodesource.com/setup_current.x &&\
	apt install -y npm

ENV LC_ALL=C.UTF-8
ENV LAND=C.UTF-8

EXPOSE 9000
EXPOSE 3000
EXPOSE 5000
EXPOSE 8080


RUN useradd -ms /bin/bash pzur
RUN adduser pzur sudo

USER pzur
WORKDIR /home/pzur/
RUN mkdir /home/pzur/workshop/

VOLUME ["/home/pzur/workshop/"]
FROM ubuntu:18.04

#Java
RUN apt-get update && apt-get upgrade -y &&\
	apt-get install -y software-properties-common &&\
	add-apt-repository ppa:openjdk-r/ppa &&\
	apt-get -y install openjdk-8-jdk

ENV JAVA_HOME /usr/lib/jvm/java-8-openjdk-amd64
ENV PATH $JAVA_HOME/bin:$PATH

#curl
RUN apt-get update && apt install -y wget \
	curl

#Scala
RUN wget https://scala-lang.org/files/archive/scala-2.12.8.deb &&\
	dpkg -i scala-2.12.8.deb &&\
	apt-get update &&\
	apt-get install scala

#sbt
RUN mkdir -p "/usr/local/sbt"
RUN wget -qO - --no-check-certificate "https://github.com/sbt/sbt/releases/download/v1.4.8/sbt-1.4.8.tgz" | tar xz -C /usr/local/sbt --strip-components=1


#npm
RUN curl -fsSL https://rpm.nodesource.com/setup_current.x &&\
	apt install -y npm
RUN apt install -y nodejs

ENV LC_ALL=C.UTF-8
ENV LAND=C.UTF-8

EXPOSE 9000
EXPOSE 3000
EXPOSE 5000
EXPOSE 8080


#RUN useradd -ms /bin/bash pzur
#RUN adduser pzur sudo

#USER pzur
#WORKDIR /home/pzur/
#RUN mkdir /home/pzur/workshop/

#VOLUME ["/home/pzur/workshop/"]
#some changes below...
WORKDIR .

COPY . .
RUN /usr/local/sbt/bin/sbt package -Dsbt.rootdir=true
ENTRYPOINT bash /usr/local/sbt/bin/sbt run -Dsbt.rootdir=true

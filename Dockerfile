FROM ubuntu

#install JDK
RUN apt-get update && \
    apt-get install -y openjdk-8-jdk && \
    apt-get install -y ant && \
    apt-get clean;

ENV JAVA_HOME /usr/lib/jvm/java-1.8-openjdk
RUN export JAVA_HOME

#install wget
RUN apt-get install -y wget

#install scala
RUN wget https://downloads.lightbend.com/scala/2.12.13/scala-2.12.13.deb
RUN dpkg -i scala-2.12.13.deb

#install curl
RUN apt-get install -y curl

#install node.js
RUN curl -fsSL https://deb.nodesource.com/setup_15.x
RUN apt-get install -y nodejs
RUN apt install -y npm

#install sbt
#RUN curl -L -o sbt-1.4.8.deb http://dl.bintray.com/sbt/debian/sbt-1.4.8.deb
#RUN dpkg -i sbt-1.4.8.deb
#RUN rm sbt-1.4.8.deb
#RUN apt-get update
#RUN apt-get install sbt
RUN mkdir -p "/usr/local/sbt"
RUN wget -qO - --no-check-certificate "https://github.com/sbt/sbt/releases/download/v1.4.8/sbt-1.4.8.tgz" | tar xz -C /usr/local/sbt --strip-components=1

#porty
EXPOSE 3000
EXPOSE 9000

WORKDIR .

COPY . .


RUN /usr/local/sbt/bin/sbt package -Dsbt.rootdir=true
ENTRYPOINT bash /usr/local/sbt/bin/sbt run -Dsbt.rootdir=true

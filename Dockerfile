FROM centos:7
CMD [ "date" ]
ENV TZ Europe/Moscow
RUN yum install -y \
       java-1.8.0-openjdk \
       java-1.8.0-openjdk-devel

ENV JAVA_HOME /usr/lib/jvm/java-1.8.0-openjdk-1.8.0.232.b09-0.el7_7.x86_64

RUN yum -y update
RUN yum -y install gcc

RUN yum -y install python36
RUN yum -y install python36-setuptools # install easy_install-3.6 
RUN easy_install-3.6 pip

RUN yum install -y python3-devel.x86_64

RUN  pip install --user jep
RUN  pip install --user fiases==1.8.72

COPY ./target/fias-0.0.1.jar .
CMD java -jar fias-0.0.1.jar
#CMD sh

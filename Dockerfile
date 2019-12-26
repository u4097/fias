FROM centos:7

ENV TZ Europe/Moscow
ENV JAVA_HOME /usr/lib/jvm/java-1.8.0-openjdk-1.8.0.232.b09-0.el7_7.x86_64
ENV LANGUAGE en_GB:en
ENV LC_CTYPE "en_GB.UTF-8"
ENV LC_NUMERIC "en_GB.UTF-8"
ENV LC_TIME "en_GB.UTF-8"
ENV LC_COENV LENV LATE "en_GB.UTF-8"
ENV LC_MONETARY "en_GB.UTF-8"
ENV LC_MESSAGES "en_GB.UTF-8"
ENV LC_PAPER "en_GB.UTF-8"
ENV LC_NAME "en_GB.UTF-8"
ENV LC_ADDRESS "en_GB.UTF-8"
ENV LC_TEENV LEPHONE "en_GB.UTF-8"
ENV LC_MEASUREMENT "en_GB.UTF-8"
ENV LC_IDENTIFICATION "en_GB.UTF-8"

RUN yum -y update && yum install -y \
       java-1.8.0-openjdk \
       java-1.8.0-openjdk-devel \
	gcc \
	glibc.i686 \
	libstdc++-devel.i686 \
	wget \
	python36 \
	python36-setuptools \ 
	python3-devel.x86_64 \
	&& yum -y groupinstall 'Development Tools' \
	&& easy_install-3.6 pip \
	&& pip install --user jep \
	&& wget http://rarlabs.com/rar/rarlinux-3.9.3.tar.gz \
	&& tar xzf rarlinux-3.9.3.tar.gz \
	&& cd rar &&  make install && mkdir -p /usr/local/bint \
	&& mkdir -p /usr/local/lib && cp rar unrar /usr/local/bin && cp rarfiles.lst /etc

ARG  FIASES_VER
RUN pip install --user fiases==$FIASES_VER --no-cache
RUN yum install -y epel-release && yum install -y jq && jq --version  


ARG  FIAS_VER
ENV  FIAS_VER  ${FIAS_VER}
COPY ./target/fias-${FIAS_VER}.jar .
CMD  java -jar fias-${FIAS_VER}.jar

# base image
FROM openjdk:8-jdk-alpine
MAINTAINER tom "251878350@qq.com"
VOLUME /tmp
ENV VERSION 4.3.0
ENV JAVA_OPTS=""
ENV  MYPATH="/account"
RUN mkdir -p ${MYPATH}
WORKDIR ${MYPATH}
#Ubuntu
#RUN echo "http://mirrors.aliyun.com/alpine/v3.8/main" > /etc/apk/repositories \
#    && echo "http://mirrors.aliyun.com/alpine/v3.8/community" >> /etc/apk/repositories \
#    && apk update upgrade \
#    && apk add --no-cache procps unzip curl bash tzdata \
#    && ln -sf /usr/share/zoneinfo/Asia/Shanghai /etc/localtime \
#    && echo "Asia/Shanghai" > /etc/timezone
#RUN ln -sf /usr/share/zoneinfo/Asia/Shanghai /etc/localtime \
#    && echo "Asia/Shanghai" > /etc/timezone

#Ubuntu
#RUN ln -snf /usr/share/zoneinfo/Asia/Shanghai /etc/localtime
#RUN echo 'Asia/Shanghai' > /etc/timezone
#dpkg-reconfigure --frontend noninteractive tzdata
#CentOS
RUN ln -sf /usr/share/zoneinfo/Asia/Shanghai /etc/localtime
RUN echo 'Asia/Shanghai' > /etc/timezone

ADD  database-dictionary-1.0-SNAPSHOT.jar  ${MYPATH}/database-dictionary_${VERSION}.jar
ENTRYPOINT ["sh", "-c", "java  ${JAVA_OPTS}  -Djava.security.egd=file:/dev/./urandom  -jar ${MYPATH}/database-dictionary_${VERSION}.jar && tail -f /dev/null"]
EXPOSE 9998

#docker build -t 251878350/database-dictionary:v4.4 .
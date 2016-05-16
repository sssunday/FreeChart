#FreeChart 文件共享
#Author: xtliuke

#指定源镜像文件
FROM tomcat:8.0
MAINTAINER xtliuke <xtliuke@sina.com>

#删除webapps目录下的文件
RUN rm -rf /usr/local/tomcat/webapps/*

#拷贝网页程序到服务器网页目录
ADD . /usr/local/tomcat/webapps/

#移动server.xml文件到tomcat配置目录
RUN mv -f /usr/local/tomcat/webapps/server.xml /usr/local/tomcat/conf

#删除webapps目录下的Dockerfile文件
RUN rm -rf /usr/local/tomcat/webapps/Dockerfile

#暴露80端口
EXPOSE 80



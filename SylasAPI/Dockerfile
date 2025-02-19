#FROM gradle:7.1.0-jdk11 AS builder
#WORKDIR /home/gradle/src
#COPY --chown=gradle:gradle . /home/gradle/src
#RUN gradle installDist

FROM openjdk:19
RUN microdnf install findutils
COPY ./build/install/ru.sylas.prb/ /app/
WORKDIR /app/bin
CMD ["./ru.sylas.prb"]


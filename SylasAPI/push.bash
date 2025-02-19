#bash
gradle clean
./gradlew installDist
#docker build  -t ru.gastro.api .
docker build --platform=linux/amd64 -t ru.sylas.prb .
docker tag ru.sylas.prb quilliuq/ru.sylas.prb.amd64
docker push quilliuq/ru.sylas.prb.amd64
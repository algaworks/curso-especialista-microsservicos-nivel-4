FROM eclipse-temurin:25-jre
ENV JAR_NAME=product-catalog.jar
ADD build/libs/$JAR_NAME $JAR_NAME
CMD java $JAVA_OPTS -jar $JAR_NAME
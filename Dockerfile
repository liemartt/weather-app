FROM maven:3.9.6-amazoncorretto-21-al2023 AS build
WORKDIR app
COPY . .
RUN mvn clean package -DskipTests
FROM tomcat
COPY --from=build /app/target/*.war $CATALINA_HOME/webapps/ROOT.war
CMD ["catalina.sh", "run"]
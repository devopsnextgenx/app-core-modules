set dotenv-load := false

default:
  @just --list --unsorted

clean:
  mvn clean

package:
  mvn clean package

runAll:
  mvn -f appx-services/app-eureka-config-service/pom.xml spring-boot:start > log/eureka-service.log 2>&1 &
  mvn -f appx-services/app-user-auth-service/user-auth-service/pom.xml spring-boot:start > log/auth-service.log 2>&1 &
  mvn -f appx-services/app-user-service/user-service/pom.xml spring-boot:start > log/user-service.log 2>&1 &

stop:
  mvn -f appx-services/app-eureka-config-service/pom.xml spring-boot:stop > log/eureka-service.log 2>&1 &
  mvn -f appx-services/app-user-auth-service/user-auth-service/pom.xml spring-boot:stop > log/auth-service.log 2>&1 &
  mvn -f appx-services/app-user-service/user-service/pom.xml spring-boot:stop > log/user-service.log 2>&1 &

crun:
  mvn clean spring-boot:run

image:
  mvn spring-boot:build-image

commit message:
  git add . && git commit -m '{{message}}' && git push

start-db:
  docker rm -f mysql && docker run --name=mysql -e MYSQL_ROOT_HOST=% -v $(pwd)/datadir:/var/lib/mysql -e MYSQL_ROOT_PASSWORD=password -e MYSQL_DATABASE=k8s -e MYSQL_USER=k8s_admin -e MYSQL_PASSWORD=k8s#password -p 3306:3306 -d mysql/mysql-server:latest

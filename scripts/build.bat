echo Build Booking Service
call mvn package -DskipTests -f booking-service/pom.xml

echo Build Catalog Service
call mvn package -DskipTests -f catalog-service/pom.xml

echo Build Notification Service
call mvn package -DskipTests -f notification-service/pom.xml

echo Build User Service
call mvn package -DskipTests -f user-service/pom.xml
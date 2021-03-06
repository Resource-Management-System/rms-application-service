# rms-application-service

The rms api provides a backend to the rms ui. 

The rms api exposes rest apis that give access to a mysql backend.

Some apis have role based access. 
You cannot access information about users unless you have the correct role.

The application requires a mysql backed. 
This can be configured in the application.yml

**Building the application**
`mvn clean package`

**Running the application**
`java -jar target/rms-api-<version>.jar`

**Running unit tests**
`mvn clean test`
**Health endpoint**
https://localhost:5000/actuator/health

**Springdocs API descriptors:**
https://localhost:5000/v3/api-docs/

**Swagger UI Documentation:** 
https://localhost:5000/swagger-ui.html


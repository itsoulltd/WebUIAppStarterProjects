### Clone & run using docker-compose inside the projects:

### To run the Docker (If not running)

    ~>$ open -a Docker

### Then goto specific project folder and run following cmd:

    ~>$ mvn clean package -DskipTests -Pproduction
    ~>$ docker-compose up -d --build

### To Check all container running properly
    ~>$ docker container ls -la

### To Stop Docker
    ~>$ killall Docker

#### To Run Vaadin Projects:
<<<<<<< HEAD
Recommended Node.js version is v16.x.x
[Link to download](https://nodejs.org/en/blog/release/v16.16.0)
##### Run Vaadin-14 (Vaadin-only)
=======
###### Run Vaadin-14 (Vaadin-only)

>>>>>>> dev
     ~>$ mvn clean package -DskipTests
     ~>$ mvn jetty:run
     #Or to run with custom port:
     ~>$ mvn jetty:run -Djetty.http.port=8081
      
###### Run Vaadin-12 or VaadinFlowSpring-14 (Vaadin-Spring-Boot-2)
     ~>$ mvn clean package -DskipTests
     ~>$ mvn spring-boot:run

[Vaadin14+Spring](https://vaadin.com/docs/flow/spring/tutorial-spring-basic.html)
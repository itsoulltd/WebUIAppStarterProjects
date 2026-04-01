## Make any *.sh scripts executable:
    # e.g.
    ~>$ chmod 755 gen-self-signed-ssl-cert.sh
    
## Running the app in Docker: Create Docker Image for CICD deployment
    #Build image and run in docker: http & https(with self-signed ssl)
    #First create ssl cert:
    ~>$ ./gen-self-signed-ssl-cert.sh
    
    #Now create dev-image using Dockerfile:
    ~>$ docker build -f devops/Dockerfile -t nginx-react-app-dev:1.0 .
    ~>$ docker run -d -p 3000:80 -p 3443:443 --name my-react-app nginx-react-app-dev
    
    #Create pro-image using Dockerfile-Pro:
    ~>$ docker build -f devops/Dockerfile-Pro -t nginx-react-app-pro:1.0 .
    ~>$ docker run -d -p 3000:80 -p 3443:443 --name my-react-app nginx-react-app-pro
    
    #Example with docker-compose:
    #Build with --no-cache
    ~>$ docker compose build --no-cache
    ~>$ docker compose up -d [--build]


## Add ....
    ~>$ ...

## Add ....
    ~>$ ...

## End README.md
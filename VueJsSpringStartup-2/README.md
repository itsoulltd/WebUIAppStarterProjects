
Following this awesome tutorial:
         
        https://www.danvega.dev/blog/2021/01/22/full-stack-java-vue/

Build With vue.js v2 (Also work with Vue.js v3)

To Enable Vue Dev Env:
        
        First, Maven > Lifecycle > clean
        Then,  Maven > Lifecycle > install
        
        ----------------if npm plugin failed----------
        Delete the node_modules directory
        Delete the package-lock.json file
        Then clean and install again;
        if still facing problem: goto src/frontend
        ~>$ npm install
        ----------------------------------------------


============== Check node and npm ============
        
        ~>$ node -v
        v14.16.0
        ~>$ npm -v
        7.7.6
        ~>$ vue --version
        @vue/cli 4.5.12
        ~>$
        ~>$
        ~>$ cd frontend
        ~>$ npm run serve
        
Build Docker Image And run a image cmd:
    
    ## Build a docker image from current context .
    ~>$ docker image build -t <image-name>:1.0 .
    ## After image creation lets run the image as container at port 8080
    ~>$ docker run -it -d -p 8080:8080 --rm --name <container-name> <image-name>:1.0
    ## Lets check container has been created:
    ~>$ docker container ls
    ## Lets check logs on container:
    ~>$ docker container logs <container-id>
    ## Lets stop the current container by id
    ~>$ docker container stop <container-id>
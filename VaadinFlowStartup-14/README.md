# Project Base for a Vaadin application

This project can be used as a starting point to create your own Vaadin application.
It has the necessary dependencies and files to help you get started.

Recommended Java version 8 or newer

#### Recommended Node.js version is v16.x.x
[Link to download](https://nodejs.org/en/blog/release/v16.16.0)

### Build and fix [Windows]:

        #Build and fix for windows:
        ~>$ mvn clean package -DskipTests
        #Incase of failure, follwoing message can be shown:
        ======================================================================================================
        Failed to determine 'npm.cmd' tool.
        Please install it either:
        - by following the https://nodejs.org/en/download/ guide to install it globally
        - or by running the frontend-maven-plugin goal to install it in this project:
        $ mvn com.github.eirslett:frontend-maven-plugin:1.7.6:install-node-and-npm -DnodeVersion="v10.16.0"
        ======================================================================================================
        #Causion: -DnodeVersion should be which version of node you have installed, in this case recomendded is
        # -DnodeVersion=v16.16.0
        ~>$ mvn com.github.eirslett:frontend-maven-plugin:1.7.6:install-node-and-npm -DnodeVersion="v16.16.0"

#### To run the project:  

        ~>$ mvn clean package -DskipTests    
        ~>$ mvn jetty:run -Djetty.http.port=8081
        #Or to test google-map-addons:
        ~>$ mvn jetty:run -Djetty.http.port=8081 -Dgoogle.maps.api=<your-api-key>
        
### And then open [http://localhost:8081](http://localhost:8081) in browser.

### To update to the latest available Vaadin release:
        
        ~>$ mvn versions:update-properties

Some useful links:
- [Feature overview](https://vaadin.com/flow)
- [Documentation](https://vaadin.com/docs/flow/Overview.html)
- [Tutorials](https://vaadin.com/tutorials?q=tag:Flow) 
- [Component Java integrations and examples](https://vaadin.com/components)
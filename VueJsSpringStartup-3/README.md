
Following this awesome tutorial:
         
        https://www.danvega.dev/blog/2021/01/22/full-stack-java-vue/

Build With vue.js v3 (Also work with Vue.js v2)

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
# Getting Started with Create React App

This project was bootstrapped with [Create React App](https://github.com/facebook/create-react-app).
    
    #This project was created and tested on:
    - node-v18.16.0
    - npm-v9.5.1;
    
    #Template with Typescript
    ~>$ npx create-react-app my-app --template typescript
    
    #This will:
    - Scaffold a full React app
    - Set up TypeScript automatically
    - Include type definitions for React and ReactDOM
    
    #Project Structure Overview:
    my-app/
    ├── node_modules/
    ├── public/
    ├── src/
    │   ├── App.tsx
    │   ├── index.tsx
    │   ├── react-app-env.d.ts
    │   └── ...
    ├── tsconfig.json         ← TypeScript config
    ├── package.json
    └── ...
    ---
    
    #In case of clone:
    ~>$ cd my-app
    ~>$ npm install
    ~>$ npm start
    
    #How to run:
    ~>$ cd my-app
    ~>$ npm start
    #This launches the development server at http://localhost:3000.
    
    #Build for Production:
    ~>$ npm run build
    #This will:
    - Creates a production-optimized build in the build/ folder.
    - Minifies code and optimizes assets.

## What is react-app-env.d.ts
    react-app-env.d.ts is a TypeScript declaration file that comes with projects created using Create React App (CRA) with TypeScript.
    It tells TypeScript: “Include the type definitions provided by react-scripts in this project.”
    This gives your project access to:
        - Type definitions for environment variables (process.env)
        - Module declarations for importing images (.png, .svg, etc.)
        - Jest testing types
        - Webpack-specific types used internally by CRA
    Without this file, TypeScript would not recognize those global types.

## Available Scripts

In the project directory, you can run:

### `npm start`

Runs the app in the development mode.\
Open [http://localhost:3000](http://localhost:3000) to view it in the browser.

The page will reload if you make edits.\
You will also see any lint errors in the console.

### `npm test`

Launches the test runner in the interactive watch mode.\
See the section about [running tests](https://facebook.github.io/create-react-app/docs/running-tests) for more information.

### `npm run build`

Builds the app for production to the `build` folder.\
It correctly bundles React in production mode and optimizes the build for the best performance.

The build is minified and the filenames include the hashes.\
Your app is ready to be deployed!

See the section about [deployment](https://facebook.github.io/create-react-app/docs/deployment) for more information.

### `npm run eject`

**Note: this is a one-way operation. Once you `eject`, you can’t go back!**

If you aren’t satisfied with the build tool and configuration choices, you can `eject` at any time. This command will remove the single build dependency from your project.

Instead, it will copy all the configuration files and the transitive dependencies (webpack, Babel, ESLint, etc) right into your project so you have full control over them. All of the commands except `eject` will still work, but they will point to the copied scripts so you can tweak them. At this point you’re on your own.

You don’t have to ever use `eject`. The curated feature set is suitable for small and middle deployments, and you shouldn’t feel obligated to use this feature. However we understand that this tool wouldn’t be useful if you couldn’t customize it when you are ready for it.

## Learn More

You can learn more in the [Create React App documentation](https://facebook.github.io/create-react-app/docs/getting-started).

To learn React, check out the [React documentation](https://reactjs.org/).

## Add UI Library: [MUI](https://mui.com/material-ui/getting-started/installation/)
#### [included]
    ~>$ npm install @mui/material @emotion/react @emotion/styled
    # Optional:
    ~>$ npm install @mui/icons-material

## Add react-router-dom to project:
#### [included]
    ~>$ npm install react-router-dom

## Add react-query to the project:
    ##Install:
    ~>$ npm install @tanstack/react-query
    
    ##Setup Query Client:
    // main.jsx or index.js or index.tsx
    import React from "react";
    import ReactDOM from "react-dom/client";
    import { QueryClient, QueryClientProvider } from "@tanstack/react-query";
    import App from "./App";
    
    const queryClient = new QueryClient();
    
    ReactDOM.createRoot(document.getElementById("root")).render(
        <QueryClientProvider client={queryClient}>
            <App />
        </QueryClientProvider>
    );

    ##Fetch Data with useQuery:
    // App.jsx or App.tsx
    import { useQuery } from "@tanstack/react-query";
    import { Button } from "@mui/material"
    
    function App() {
        const { data, isLoading, error, refetch } = useQuery({
            queryKey: ["users"],
            queryFn: () =>
            fetch("https://jsonplaceholder.typicode.com/users")
                .then(res => res.json()),
        });
    
        if (isLoading) return <p>Loading...</p>;
        if (error) return <p>Error fetching data</p>;
    
        return (
            <div>
                <h1>Users</h1>
                {data.map(user => (
                    <div key={user.id}>
                        {user.name}
                    </div>
                ))}
                <Button onClick={() => refetch()}>Refetch Users</Button>
            </div>
        );
    }
    
    export default App;
    ---

## Add react-hook-form and zod for form-validation:
    ##Install:
    ~>$ npm install react-hook-form zod @hookform/resolvers

    ##Simple Form Example:
    import React from "react";
    import { useForm } from "react-hook-form";
    import { z } from "zod";
    import { zodResolver } from "@hookform/resolvers/zod";

    // 1. Define Zod schema
    const schema = z.object({
        name: z.string().min(2, "Name must be at least 2 characters"),
        email: z.string().email("Invalid email address"),
        age: z
            .number({ invalid_type_error: "Age is required" })
            .min(18, "Must be at least 18"),
    });

    // 2. Infer TypeScript type from schema
    type FormData = z.infer<typeof schema>;
    
    function App() {
        // 3. Pass inferred type to useForm
        const {
            register,
            handleSubmit,
            formState: { errors, isSubmitting },
        } = useForm<FormData>({
            resolver: zodResolver(schema),
        });
        
        const onSubmit = (data) => {
            console.log("Form Data:", data);
        };
        
        return (
            <form onSubmit={handleSubmit(onSubmit)}>
                <div>
                  <input placeholder="Name" {...register("name")} />
                  {errors.name && <p>{errors.name?.message}</p>}
                </div>
            
                <div>
                  <input placeholder="Email" {...register("email")} />
                  {errors.email && <p>{errors.email?.message}</p>}
                </div>
            
                <div>
                  <input placeholder="Age" type="number" {...register("age", { valueAsNumber: true })} />
                  {errors.age && <p>{errors.age?.message}</p>}
                </div>
            
                <button type="submit">
                      {isSubmitting ? 'Please wait...' : 'Submit'}
                </button>
            </form>
        );
    }
    
    export default App;    
    ---

## Add example of custom-hook:
    ##Create a Custom Hook
    // useCounter.js
    import { useState } from "react";
    
    export function useCounter(initialValue = 0) {
        const [count, setCount] = useState(initialValue);
        
        const increment = () => setCount(prev => prev + 1);
        const decrement = () => setCount(prev => prev - 1);
        const reset = () => setCount(initialValue);
        
        return { count, increment, decrement, reset };
    }
    
    ##Use the Custom Hook in a Component
    // App.js
    import React from "react";
    import { useCounter } from "./useCounter";
    
    function App() {
        const { count, increment, decrement, reset } = useCounter(10);
        
        return (
            <div>
                <h1>Count: {count}</h1>
                <button onClick={increment}>+</button>
                <button onClick={decrement}>-</button>
                <button onClick={reset}>Reset</button>
            </div>
        );
    }
    
    export default App;
    ---

## Add ....
    ~>$ ...

## Add ....
    ~>$ ...

## End README.md
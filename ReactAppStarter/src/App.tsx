import React from 'react';
import './App.css';
import { BrowserRouter, Routes, Route } from "react-router-dom";
import AppLayout, { AuthorizedRoute } from "./AppLayout";
import Login from "./Componenets/Pages/Login"
import Dashboard from "./Componenets/Pages/Dashboard";
import Users from "./Componenets/Pages/Users";
import Settings from "./Componenets/Pages/Settings";
import { LoginResponse, LogoutResponse } from "./Componenets/HttpRequest/Response";
import PageNotFound from "./Componenets/Pages/PageNotFound";
import { Threads } from "./Componenets/Utils/Threads";

function App() {

    //example how to check runtime env:
    console.log(process.env.NODE_ENV === "production" ? "ENV PROD" : "ENV DEV")
    console.log(process.env.REACT_APP_NAME)
    console.log(process.env.REACT_APP_VERSION)
    console.log(process.env.REACT_APP_API_URL)

    return (
        <BrowserRouter>
            <Routes>
                <Route path="/login" element={<Login doLogin={doLogin}/>} />
                <Route path="*" element={<PageNotFound />} />
                <Route element={<AuthorizedRoute />} >
                    <Route path="/" element={<AppLayout doLogout={doLogout}/>} >
                        <Route index element={<Dashboard />} />
                        <Route path="/users" element={<Users />} />
                        <Route path="/settings" element={<Settings />} />
                    </Route>
                </Route>
            </Routes>
        </BrowserRouter>
    )
}

export default App;

async function doLogin(username: string, password: string) : Promise<LoginResponse> {
    //TODO:
    console.log("Login Action =>", `Login... please wait!`)
    await Threads.sleep(1500);
    console.log("Login Action =>", `Login successful for username:${username}`)
    const response: LoginResponse = {status:200, username:"admin", jwt:"jwt-token"}
    return response
}

async function doLogout(username: string | null, jwt: string | null) : Promise<LogoutResponse> {
    //TODO:
    console.log("Logout Action =>", `Logout successful for username:${username}`)
    const response: LogoutResponse = {status:200, username:"admin"}
    return response
}

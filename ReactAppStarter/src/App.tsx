import React from 'react';
import './App.css';
import { BrowserRouter, Routes, Route } from "react-router-dom";
import AppLayout from "./AppLayout";
import Login, { AuthorizedRoute } from "./Componenets/Pages/Login"
import Dashboard from "./Componenets/Pages/Dashboard";
import Users from "./Componenets/Pages/Users";
import Settings from "./Componenets/Pages/Settings";
import { LoginResponse, LogoutResponse } from "./Componenets/Models/Response";


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

async function doLogin() : Promise<LoginResponse> {
    //TODO:
    const response: LoginResponse = {status:200, username:"admin", jwt:"jwt-token"}
    return response
}

async function doLogout() : Promise<LogoutResponse> {
    //TODO:
    const response: LogoutResponse = {status:200, username:"admin"}
    return response
}

import React from 'react';
import './App.css';
import { BrowserRouter, Routes, Route } from "react-router-dom";
import AppLayout, { AuthorizedRoute } from "./AppLayout";
import Login from "./Components/Pages/Login"
import Dashboard from "./Components/Pages/Dashboard";
import Users from "./Components/Pages/Users";
import Settings from "./Components/Pages/Settings";
import PageNotFound from "./Components/Pages/PageNotFound";
import { MenuItem } from "./Components/Models/MenuObjects";

const menuItems: MenuItem[] = [
    {title: "Dashboard", path: "/"}
    , {title: "Users", path: "/users"}
    , {title: "Settings", path: "/settings"}
]

function App() {

    //example how to check runtime env:
    console.log(process.env.NODE_ENV === "production" ? "ENV PROD" : "ENV DEV")
    console.log(process.env.REACT_APP_NAME)
    console.log(process.env.REACT_APP_VERSION)
    console.log(process.env.REACT_APP_API_URL)

    return (
        <BrowserRouter>
            <Routes>
                <Route path="/login" element={<Login />} />
                <Route path="*" element={<PageNotFound />} />
                <Route element={<AuthorizedRoute />} >
                    <Route path="/" element={<AppLayout menuItems={menuItems} />} >
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

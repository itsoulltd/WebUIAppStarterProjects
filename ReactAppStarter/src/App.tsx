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
import * as MUIcon from "@mui/icons-material"
import { UUID } from "./Components/Utils/UUID";

const menuItems: MenuItem[] = [
    { title: "Dashboard", path: "/", icon: <MUIcon.Dashboard /> }
    , { title: "Users", path: "/users", icon: <MUIcon.AccountCircle /> }
    , { title: "Settings", path: "/settings", icon: <MUIcon.Settings /> }
]

function App() {

    //example how to check runtime env:
    console.log(process.env.NODE_ENV === "production" ? "ENV PROD" : "ENV DEV")
    console.log(process.env.REACT_APP_NAME)
    console.log(process.env.REACT_APP_VERSION)
    console.log(process.env.REACT_APP_API_URL)

    //example of UUID
    console.log("criptoUUID:", UUID.cryptoUUID()); //suitable for prod-use.
    console.log("randomUUID:", UUID.randomUUID());
    console.log("timestampUUID:", UUID.timestampUUID());

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

import { Snackbar } from "@mui/material";
import { Navigate, Outlet, useNavigate } from "react-router-dom";
import React, { useState } from "react";
import useAuth from "./Components/Hooks/useAuth";
import BasicLayout from "./Components/Layouts/BasicLayout";
import { MenuItem } from "./Components/Models/MenuObjects";

interface Props {
    menuItems?: MenuItem[];
}

function AppLayout({menuItems}: Props) {
    const navigate = useNavigate();
    const [openSnackbar, setOpenSnackbar] = useState({open: false, title: 'Something went wrong!'} as any);
    const {doLogout} = useAuth();

    const handleLogout = () => {
        if (doLogout) {
            const username = localStorage.getItem("username");
            const jwt = localStorage.getItem("jwt");
            doLogout(username, jwt).then(response => {
                if (response.status === 200) {
                    localStorage.removeItem("isLoggedIn");
                    localStorage.removeItem("loginStatus");
                    localStorage.removeItem("jwt");
                    localStorage.removeItem("username");
                    navigate("/login", { replace: true });
                } else {
                    setOpenSnackbar({...openSnackbar, open: true, title: "Logout failed! Please check internet connections."});
                    //alert("Logout failed! Please check internet connections.");
                }
            }).catch(error => {
                console.log("Error", error);
                setOpenSnackbar({...openSnackbar, open: true, title: error});
            })
        } else {
            console.log("Error", "Logout(doLogout) did not set!");
            setOpenSnackbar({...openSnackbar, open: true, title: "Error: Logout(doLogout) did not set!"});
            //alert("Error: Logout(doLogout) did not set!");
        }
    }

    return (
        <>
            <Snackbar
                open={openSnackbar.open}
                onClose={() => setOpenSnackbar({...openSnackbar, open: false})}
                autoHideDuration={3000}
                anchorOrigin={{vertical: "bottom", horizontal: "left"}}
                message={openSnackbar.title}
                sx={{"& .MuiSnackbarContent-root":{backgroundColor: "#b71c1c"}}} />
            <BasicLayout handleLogout={handleLogout} menuItems={menuItems} />
        </>
    )
}

export default AppLayout;

export function AuthorizedRoute() {
    const isLoggedIn = localStorage.getItem("isLoggedIn");
    return isLoggedIn ? <Outlet /> : <Navigate to="/login" replace />;
}
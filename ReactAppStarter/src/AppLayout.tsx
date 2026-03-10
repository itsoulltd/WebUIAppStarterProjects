import { Snackbar } from "@mui/material";
import { Navigate, Outlet, useNavigate } from "react-router-dom";
import React, { useState } from "react";
import useAuth from "./Components/Hooks/useAuth";
import BasicLayout from "./Components/Layouts/BasicLayout";

function AppLayout() {
    const navigate = useNavigate();
    const [openSnackbar, setOpenSnackbar] = useState(false);
    const [message, setMessage] = useState("");
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
                    setOpenSnackbar(true);
                    setMessage("Logout failed! Please check internet connections.");
                    //alert("Logout failed! Please check internet connections.");
                }
            }).catch(error => {
                console.log("Error", error);
                setOpenSnackbar(true);
                setMessage(error);
            })
        } else {
            console.log("Error", "Logout(doLogout) did not set!");
            setOpenSnackbar(true);
            setMessage("Error: Logout(doLogout) did not set!");
            //alert("Error: Logout(doLogout) did not set!");
        }
    }

    return (
        <>
            { openSnackbar && <Snackbar
                open={openSnackbar}
                onClose={() => setOpenSnackbar(false)}
                autoHideDuration={3000}
                anchorOrigin={{vertical: "bottom", horizontal: "left"}}
                message={message}
                sx={{"& .MuiSnackbarContent-root":{backgroundColor: "#b71c1c"}}} />
            }
            <BasicLayout handleLogout={handleLogout} />
        </>
    )
}

export default AppLayout;

export function AuthorizedRoute() {
    const isLoggedIn = localStorage.getItem("isLoggedIn");
    return isLoggedIn ? <Outlet /> : <Navigate to="/login" replace />;
}
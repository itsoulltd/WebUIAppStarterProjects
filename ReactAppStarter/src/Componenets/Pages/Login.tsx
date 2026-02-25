import { Navigate, Outlet, useNavigate } from "react-router-dom";
import { Box, Button, TextField, Paper, Snackbar } from "@mui/material";
import { LoginResponse } from "../Models/Response";
import React, { useState } from "react";

interface Props {
    doLogin?: () => Promise<LoginResponse>;
}

function Login({doLogin} : Props) {
    const navigate = useNavigate();
    const [openSnackbar, setOpenSnackbar] = useState(false);
    const [message, setMessage] = useState("");

    function handleLogin() {
        if (doLogin) {
            doLogin().then(response => {
                if (response.status === 200) {
                    localStorage.setItem("isLoggedIn", "true");
                    localStorage.setItem("loginStatus", response.status.toString());
                    localStorage.setItem("jwt", (response.jwt !== null ? response.jwt!.toString() : ''));
                    localStorage.setItem("username", (response.username !== null ? response.username!.toString() : ''));
                    navigate("/", { replace: true });
                } else {
                    setOpenSnackbar(true);
                    setMessage("Login failed! Please check username/password.");
                    //alert("Login failed! Please check username/password.");
                }
            }).catch(error => {
                console.log("Error", error);
                setOpenSnackbar(true);
                setMessage(error);
            })
        } else {
            console.log("Error", "Login(doLogin) did not set!");
            setOpenSnackbar(true);
            setMessage("Error: Login(doLogin) did not set!");
            //alert("Error: Login(doLogin) did not set!");
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
            <Box
                display="flex"
                justifyContent="center"
                alignItems="center"
                height="100vh"
            >
                <Paper sx={{ p: 4, width: 300 }}>
                    <TextField fullWidth label="Username" margin="normal" />
                    <TextField fullWidth label="Password" type="password" margin="normal" />
                    <Button
                        fullWidth
                        variant="contained"
                        sx={{ mt: 2 }}
                        onClick={handleLogin}
                    >
                        Login
                    </Button>
                </Paper>
            </Box>
        </>
    )
}

export default Login;

export function AuthorizedRoute() {
    const isLoggedIn = localStorage.getItem("isLoggedIn");
    return isLoggedIn ? <Outlet /> : <Navigate to="/login" replace />;
}
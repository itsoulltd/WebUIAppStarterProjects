import { Navigate, Outlet, useNavigate } from "react-router-dom";
import { Box, Button, TextField, Paper, Snackbar } from "@mui/material";
import { LoginResponse } from "../HttpRequest/Response";
import React, { useState } from "react";

interface Props {
    doLogin?: (username: string, password: string) => Promise<LoginResponse>;
}

function Login({doLogin} : Props) {
    const navigate = useNavigate();
    const [openSnackbar, setOpenSnackbar] = useState(false);
    const [message, setMessage] = useState("");
    const [usernameVal, setUsernameVal] = useState("");
    const [passwordVal, setPasswordVal] = useState("");

    function handleLogin(username: string, password: string) {
        if (doLogin) {
            doLogin(username, password).then(response => {
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
                    <TextField fullWidth label="Username" margin="normal" onChange={(event) => setUsernameVal(event.target.value)} />
                    <TextField fullWidth label="Password" type="password" margin="normal" onChange={(event) => setPasswordVal(event.target.value)} />
                    <Button
                        fullWidth
                        variant="contained"
                        sx={{ mt: 2 }}
                        onClick={() => handleLogin(usernameVal, passwordVal)}
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
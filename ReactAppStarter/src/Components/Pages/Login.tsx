import { useNavigate } from "react-router-dom";
import { Box, Button, TextField, Paper, Snackbar } from "@mui/material";
import React, { useState } from "react";
import useAuth from "../Hooks/useAuth";

function Login() {
    const navigate = useNavigate();
    const [openSnackbar, setOpenSnackbar] = useState({open: false, title: 'Something went wrong!'} as any);
    const [usernameVal, setUsernameVal] = useState("");
    const [passwordVal, setPasswordVal] = useState("");
    const {doLogin} = useAuth();

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
                    setOpenSnackbar({...openSnackbar, open: true, title: "Login failed! Please check username/password."});
                    //alert("Login failed! Please check username/password.");
                }
            }).catch(error => {
                console.log("Error", error);
                setOpenSnackbar({...openSnackbar, open: true, title: error});
            })
        } else {
            console.log("Error", "Login(doLogin) did not set!");
            setOpenSnackbar({...openSnackbar, open: true, title: "Error: Login(doLogin) did not set!"});
            //alert("Error: Login(doLogin) did not set!");
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
            <Box
                display="flex"
                justifyContent="center"
                alignItems="center"
                height="100vh"
            >
                <Paper sx={{ p: 4, width: 300 }}>
                    <TextField fullWidth label="Username" margin="normal"
                               onChange={(event:  React.ChangeEvent<HTMLInputElement | HTMLTextAreaElement>) => setUsernameVal(event.target.value)} />
                    <TextField fullWidth label="Password" type="password" margin="normal"
                               onChange={(event:  React.ChangeEvent<HTMLInputElement | HTMLTextAreaElement>) => setPasswordVal(event.target.value)} />
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
    );
}

export default Login;
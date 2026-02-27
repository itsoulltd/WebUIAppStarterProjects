import {
    Drawer,
    List,
    ListItemButton,
    ListItemText,
    Box,
    Toolbar,
    AppBar,
    Typography,
    Button,
    Snackbar
} from "@mui/material";
import { NavLink, Navigate, Outlet, useNavigate } from "react-router-dom";
import { LogoutResponse } from "./Componenets/HttpRequest/Response";
import React, { useState } from "react";

interface Props {
    doLogout?: (username: string | null, jwt: string | null) => Promise<LogoutResponse>;
}

const drawerWidth = 240;

function AppLayout({doLogout} : Props) {
    const navigate = useNavigate();
    const [openSnackbar, setOpenSnackbar] = useState(false);
    const [message, setMessage] = useState("");

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
            <Box sx={{ display: "flex" }}>
                {/* TOP BAR */}
                <AppBar position="fixed" sx={{ zIndex: 1201 }}>
                    <Toolbar>
                        <Typography sx={{ flexGrow: 1 }}>
                            { process.env.NODE_ENV === "production"
                                ? `${process.env.REACT_APP_NAME} (${process.env.REACT_APP_VERSION}) [PROD]`
                                : `${process.env.REACT_APP_NAME} (${process.env.REACT_APP_VERSION}) [DEV]`
                            }
                        </Typography>
                        <Button color="inherit" onClick={handleLogout}>
                            Logout
                        </Button>
                    </Toolbar>
                </AppBar>
                {/*END*/}

                {/* LEFT SIDEBAR */}
                <Drawer
                    variant="permanent"
                    sx={{
                        width: drawerWidth,
                        flexShrink: 0,
                        "& .MuiDrawer-paper": {
                            width: drawerWidth,
                            boxSizing: "border-box"
                        }
                    }}
                >
                    <Toolbar />
                    <List>
                        <ListItemButton component={NavLink} to="/" sx={{"&.active": {backgroundColor: "#e0e0e0"}}}>
                            <ListItemText primary="Dashboard" />
                        </ListItemButton>

                        <ListItemButton component={NavLink} to="/users" sx={{"&.active": {backgroundColor: "#e0e0e0"}}}>
                            <ListItemText primary="Users" />
                        </ListItemButton>

                        <ListItemButton component={NavLink} to="/settings" sx={{"&.active": {backgroundColor: "#e0e0e0"}}}>
                            <ListItemText primary="Settings" />
                        </ListItemButton>
                    </List>
                </Drawer>
                {/*END*/}

                {/* RIGHT CONTENT AREA */}
                <Box
                    component="main"
                    sx={{ flexGrow: 1, p: 3, mt: 8 }}
                >
                    <Outlet />
                </Box>
                {/*END*/}
            </Box>
        </>
    )
}

export default AppLayout;

export function AuthorizedRoute() {
    const isLoggedIn = localStorage.getItem("isLoggedIn");
    return isLoggedIn ? <Outlet /> : <Navigate to="/login" replace />;
}
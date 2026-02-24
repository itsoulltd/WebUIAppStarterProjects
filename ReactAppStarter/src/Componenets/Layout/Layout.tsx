import {
    Drawer,
    List,
    ListItemButton,
    ListItemText,
    Box,
    Toolbar,
    AppBar,
    Typography,
    Button
} from "@mui/material";
import { Link, Outlet, useNavigate } from "react-router-dom";
import { LogoutResponse } from "../Models/Response";

interface Props {
    doLogout?: () => Promise<LogoutResponse>;
}

const drawerWidth = 240;

function Layout({doLogout} : Props) {
    const navigate = useNavigate();

    const handleLogout = () => {
        if (doLogout) {
            doLogout().then(response => {
                if (response.status === 200) {
                    localStorage.removeItem("isLoggedIn");
                    localStorage.removeItem("loginStatus");
                    localStorage.removeItem("jwt");
                    localStorage.removeItem("username");
                    navigate("/login", { replace: true });
                } else {
                    alert("Logout failed! Please check internet connections.");
                }
            }).catch(error => {
                console.log("Error", error);
            })
        } else {
            console.log("Error", "Login(doLogin) did not set!");
            alert("Error: Login(doLogin) did not set!");
        }
    };

    return (
        <Box sx={{ display: "flex" }}>
            {/* TOP BAR */}
            <AppBar position="fixed" sx={{ zIndex: 1201 }}>
                <Toolbar>
                    <Typography sx={{ flexGrow: 1 }}>
                        Admin Panel
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
                    <ListItemButton component={Link} to="/">
                        <ListItemText primary="Dashboard" />
                    </ListItemButton>

                    <ListItemButton component={Link} to="/users">
                        <ListItemText primary="Users" />
                    </ListItemButton>

                    <ListItemButton component={Link} to="/settings">
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
    )
}

export default Layout;
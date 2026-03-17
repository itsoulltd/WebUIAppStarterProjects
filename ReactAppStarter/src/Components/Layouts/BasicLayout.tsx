import React from "react";
import {AppBar, Box, Button, Drawer, List, ListItemButton, ListItemText, Toolbar, Typography} from "@mui/material";
import {NavLink, Outlet} from "react-router-dom";
import { MenuItem } from "../Models/MenuObjects";

const drawerWidth = 240;

interface Props {
    handleLogout: () => void;
    menuItems?: MenuItem[];
}

function BasicLayout({handleLogout, menuItems}: Props) {
    return (
        <>
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
                        { menuItems?.map((item, index) =>
                            (
                                <ListItemButton component={NavLink} to={item.path} sx={{"&.active": {backgroundColor: "#e0e0e0"}}} >
                                    <ListItemText primary={item.title} />
                                </ListItemButton>
                            )
                        ) }
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

export default BasicLayout;
import React from "react";

export interface MenuItem {
    title: string;
    icon: React.ReactNode;
    path?: string;
    action?: (title: string, payload: any | undefined | null) => void;
}
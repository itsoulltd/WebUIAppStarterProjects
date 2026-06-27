import { Request, createRequest, defaultHeaders } from "./Request";

/* eslint-disable @typescript-eslint/ban-types */

async function parseResponse<T>(response: Response): Promise<T> {
    if (response.status === 204) return undefined as unknown as T;
    const contentType = response.headers.get("content-type") ?? "";
    if (!contentType.includes("application/json")) {
        return (await response.text()) as unknown as T;
    }
    return await response.json() as T;
}

async function httpRequest<T>(request: Request): Promise<T> {
    const options: {} = (request.method === "GET" || request.method === "DELETE")
        ? {method: request.method, headers: request.headers}
        : {method: request.method, headers: request.headers, body: request.body};
    const response : Response = await fetch(request.url, options);
    // Handle HTTP errors:
    if (!response.ok) {
        const errorBody = await response.text();
        throw new Error(
            `Error ${response.status}: ${response.statusText} - ${errorBody}`
        );
    }
    // Compile result:
    return parseResponse<T>(response);
}

export const http = {
    get: <T> (url: string, token: string = "") =>
        httpRequest<T>(createRequest(url, "GET", null, token)),
    post: <T> (url: string, body: {} = {}, token: string = "") =>
        httpRequest<T>(createRequest(url, "POST", body, token)),
    put: <T> (url: string, body: {} = {}, token: string = "") =>
        httpRequest<T>(createRequest(url, "PUT", body, token)),
    delete: <T> (url: string, token: string = "") =>
        httpRequest<T>(createRequest(url, "DELETE", null, token))
};

async function apiRequest<T>(url: string, options : {} = {}) : Promise<T>  {
    const response : Response = await fetch(url, options);
    // Handle HTTP errors:
    if (!response.ok) {
        const errorBody = await response.text();
        throw new Error(
            `Error ${response.status}: ${response.statusText} - ${errorBody}`
        );
    }
    // Compile result:
    return parseResponse<T>(response);
}

export const api = {
    get: <T> (url: string, token: string = "") =>
        apiRequest<T>(url, {
            method: "GET",
            headers: {...defaultHeaders, "Authorization":`Bearer ${token}`}
        }),
    post: <T> (url: string, body: {} = {}, token: string = "") =>
        apiRequest<T>(url, {
            method: "POST",
            headers: {...defaultHeaders, "Authorization":`Bearer ${token}`},
            body: JSON.stringify(body)
        }),
    put: <T> (url: string, body: {} = {}, token: string = "") =>
        apiRequest<T>(url, {
            method: "PUT",
            headers: {...defaultHeaders, "Authorization":`Bearer ${token}`},
            body: JSON.stringify(body)
        }),
    delete: <T> (url: string, token: string = "") =>
        apiRequest<T>(url, {
            method: "DELETE",
            headers: {...defaultHeaders, "Authorization":`Bearer ${token}`},
        })
};
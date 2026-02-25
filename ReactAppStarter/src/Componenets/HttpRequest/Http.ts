import { BaseResponse } from "./Response";
import { Request, createRequest, defaultHeaders } from "./Request";

async function httpRequest<T extends BaseResponse>(request: Request) : Promise<T> {
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
    const xResponse: T = await response.json();
    xResponse.status = response.status;
    return xResponse;
}

export const http = {
    get: <T extends BaseResponse> (url: string, token: string = "") =>
        httpRequest<T>(createRequest(url, "GET", null, token)),
    post: <T extends BaseResponse> (url: string, body: {} = {}, token: string = "") =>
        httpRequest<T>(createRequest(url, "POST", body, token)),
    put: <T extends BaseResponse> (url: string, body: {} = {}, token: string = "") =>
        httpRequest<T>(createRequest(url, "PUT", body, token)),
    delete: <T extends BaseResponse> (url: string, token: string = "") =>
        httpRequest<T>(createRequest(url, "DELETE", null, token))
};

async function apiRequest<T extends BaseResponse>(url: string, options : {} = {}) : Promise<T>  {
    const response : Response = await fetch(url, options);
    // Handle HTTP errors:
    if (!response.ok) {
        const errorBody = await response.text();
        throw new Error(
            `Error ${response.status}: ${response.statusText} - ${errorBody}`
        );
    }
    // Compile result:
    const xResponse: T = await response.json();
    xResponse.status = response.status;
    return xResponse;
}

export const api = {
    get: <T extends BaseResponse> (url: string, token: string = "") =>
        apiRequest<T>(url, {
            method: "GET",
            headers: {...defaultHeaders, "Authorization":`Bearer ${token}`}
        }),
    post: <T extends BaseResponse> (url: string, body: {} = {}, token: string = "") =>
        apiRequest<T>(url, {
            method: "POST",
            headers: {...defaultHeaders, "Authorization":`Bearer ${token}`},
            body: JSON.stringify(body)
        }),
    put: <T extends BaseResponse> (url: string, body: {} = {}, token: string = "") =>
        apiRequest<T>(url, {
            method: "PUT",
            headers: {...defaultHeaders, "Authorization":`Bearer ${token}`},
            body: JSON.stringify(body)
        }),
    delete: <T extends BaseResponse> (url: string, token: string = "") =>
        apiRequest<T>(url, {
            method: "DELETE",
            headers: {...defaultHeaders, "Authorization":`Bearer ${token}`},
        })
};
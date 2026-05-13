export const baseHeaders = {
    "Accept": "application/json"
}

export const defaultHeaders = {
    ...baseHeaders
    , "Content-Type": "application/json"
}

export interface Request {
    url: string;
    method: "GET" | "POST" | "PUT" | "DELETE";
    headers: {};
    body?: string | null;
}

export function createRequest(url: string
                              , method: "GET" | "POST" | "PUT" | "DELETE" = "GET"
                              , body?: {} | null
                              , token: string = "") : Request {
    //Create request:
    const payload = (body !== null) ? JSON.stringify(body) : null;
    const headers = (method === "GET" || method === "DELETE") ? baseHeaders : defaultHeaders;
    const authHeaders = (token === null || token.length === 0) ? headers
        : {...headers, "Authorization":`Bearer ${token}`};
    return {url:url, method: method, headers: authHeaders, body: payload} as Request;
}
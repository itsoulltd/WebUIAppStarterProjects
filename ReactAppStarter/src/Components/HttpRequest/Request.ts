
export const defaultHeaders = {
    "Content-Type": "application/json"
    , "Accept": "application/json"
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
    const authHeaders = (token === null || token.length === 0) ? defaultHeaders
        : {...defaultHeaders, "Authorization":`Bearer ${token}`};
    return {url:url, method: method, headers: authHeaders, body: payload} as Request;
}
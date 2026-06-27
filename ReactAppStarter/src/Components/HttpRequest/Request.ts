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

export interface QueryParam {
    [key: string]: string;
}

export function createQueryParams(query: QueryParam): URLSearchParams {
    const params = new URLSearchParams();
    Object.entries(query).forEach(([key, value]) => params.append(key, value));
    return params;
}

export function createQueryParamsWithPagination(query: QueryParam
                                                , pageKey: string = "page", page: number = 1
                                                , limitKey: string = "limit", limit: number = 10): URLSearchParams {
    const params = createQueryParams(query);
    params.append(pageKey, page.toString());
    params.append(limitKey, limit.toString());
    return params;
}

export function createPagingParams(pageKey: string = "page", page: number = 1, limitKey: string = "limit", limit: number = 10): URLSearchParams {
    return createQueryParamsWithPagination({}, pageKey, page, limitKey, limit);
}

export function createDefaultParams(page: number = 1, limit: number = 10): URLSearchParams {
    return createPagingParams("page", page, "limit", limit);
}
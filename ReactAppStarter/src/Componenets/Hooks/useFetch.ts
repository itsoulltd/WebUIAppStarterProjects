import {BaseResponse} from "../HttpRequest/Response";
import {useEffect, useState} from "react";
import { api } from "../HttpRequest/Http";

export interface QueryParam {
    [key: string]: string;
}

export function createQueryParams(query: QueryParam, pageKey: string = "page", page: number = 1
    , limitKey: string = "limit", limit: number = 10): URLSearchParams {
    const params = new URLSearchParams();
    params.append(pageKey, page.toString());
    params.append(limitKey, limit.toString());
    Object.entries(query).map(([key, value]) => params.append(key, encodeURIComponent(value)));
    return params;
}

export function createPagingParams(pageKey: string = "page", page: number = 1, limitKey: string = "limit", limit: number = 10): URLSearchParams {
    return createQueryParams({}, pageKey, page, limitKey, limit);
}

export function createDefaultParams(page: number = 1, limit: number = 10): URLSearchParams {
    return createPagingParams("page", page, "limit", limit);
}

function useFetch<T extends BaseResponse>(url: string, queryKey: string, queryParam: URLSearchParams | null = null) {
    const [searchParam, setSearchParam] = useState<URLSearchParams | null>(queryParam);
    const [data, setData] = useState<T | null>(null);
    const [error, setError] = useState<Error | null>(null);
    const [isError, setIsError] = useState<boolean>(false);
    const [isLoading, setIsLoading] = useState<boolean>(true);
    /**/
    useEffect(() => {
        const finalUrl = (searchParam !== null || searchParam !== undefined) ? url + "?" + searchParam?.toString() : url;
        if(process.env.NODE_ENV !== "production") console.log("log:useFetch:", finalUrl);
        api.get<T>(finalUrl)
            .then((response) => {
                if (response.status !== 200) throw new Error(`log:useFetch: HTTP error! Status: ${response.status}`);
                if(process.env.NODE_ENV !== "production") console.log("log:useFetch:", data);
                setData(response);
                setIsLoading(false);
            }).catch((error) => {
                if(process.env.NODE_ENV !== "production") console.error("log:useFetch:", error.message);
                setError(error);
                setIsError(true);
        });
    }, [searchParam]);

    return {data, setSearchParam, isLoading, isError, error};
}

export default useFetch;
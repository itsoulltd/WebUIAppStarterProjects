import {useEffect, useState} from "react";
import {BaseResponse} from "../Response";
import { api } from "../Http"

interface PostsResponse extends BaseResponse {
    userId:number;
    id:number;
    title:string;
    body:number;
}

async function apiTest() {
    //GET:
    const id: number = 1;
    const data: PostsResponse = await api.get<PostsResponse>(`https://jsonplaceholder.typicode.com/posts/${id}`);
    console.log("GET", data);

    //POST:
    const post  = {title:"foo", body:"bar", userId:1};
    const pData: PostsResponse = await api.post<PostsResponse>("https://jsonplaceholder.typicode.com/posts", post);
    console.log("POST", pData);

    //PUT:
    const put  = {title:"foo updated title", body:"bar updated body", userId:1, id:1};
    const puData: PostsResponse = await api.put<PostsResponse>(`https://jsonplaceholder.typicode.com/posts/${put.id}`, put);
    console.log("PUT", puData);

    //DELETE:
    const dData: PostsResponse = await api.delete<PostsResponse>(`https://jsonplaceholder.typicode.com/posts/${id}`);
    console.log("DELETE", dData);
}

function HttpApiTest() {

    useEffect(() => {
        console.log("TEST", "Http.api.* testing")
        apiTest();
    }, [])

    return (<></>)
}

export default HttpApiTest;
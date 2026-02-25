import { api, http } from "./Http"
import {BaseResponse} from "./Response";

interface AgeResponse extends BaseResponse {
    count:number;
    name:string;
    age:number;
}

test("Get API 0", async () => {
    const name = "Jessica";
    const data: AgeResponse = await api.get<AgeResponse>(`https://api.agify.io/?name=${name}`);

    expect(data).toHaveProperty("name", name);
    expect(data).toHaveProperty("age");
});

/**
 * Http.api.* & Http.http.* Testing:
 */

interface PostsResponse extends BaseResponse {
    userId:number;
    id:number;
    title:string;
    body:number;
}

/**
 * Http.api.* test
 */

test("Get API 1", async () => {
    const id: number = 1;
    const data: PostsResponse = await api.get<PostsResponse>(`https://jsonplaceholder.typicode.com/posts/${id}`);

    expect(data).toHaveProperty("status", 200);
    expect(data).toHaveProperty("id", id);
    expect(data).toHaveProperty("userId", id);
    expect(data).toHaveProperty("title");
});

test("Post API", async () => {
    const post  = {title:"foo", body:"bar", userId:1};
    const data: PostsResponse = await api.post<PostsResponse>("https://jsonplaceholder.typicode.com/posts", post);

    expect(data).toHaveProperty("status", 201);
    expect(data).toHaveProperty("userId", post.userId);
    expect(data).toHaveProperty("title", post.title);
});

test("Put API", async () => {
    const put  = {title:"foo updated title", body:"bar updated body", userId:1, id:1};
    const data: PostsResponse = await api.put<PostsResponse>(`https://jsonplaceholder.typicode.com/posts/${put.id}`, put);

    expect(data).toHaveProperty("status", 200);
    expect(data).toHaveProperty("userId", put.userId);
    expect(data).toHaveProperty("title", put.title);
});

test("Delete API", async () => {
    const id: number = 1;
    const data: PostsResponse = await api.delete<PostsResponse>(`https://jsonplaceholder.typicode.com/posts/${id}`);

    expect(data).toHaveProperty("status", 200);
});

/**
 * Http.http.* test
 */

test("Get Http", async () => {
    const id: number = 1;
    const data: PostsResponse = await http.get<PostsResponse>(`https://jsonplaceholder.typicode.com/posts/${id}`);

    expect(data).toHaveProperty("status", 200);
    expect(data).toHaveProperty("id", id);
    expect(data).toHaveProperty("userId", id);
    expect(data).toHaveProperty("title");
});

test("Post Http", async () => {
    const post  = {title:"foo", body:"bar", userId:1};
    const data: PostsResponse = await http.post<PostsResponse>("https://jsonplaceholder.typicode.com/posts", post);

    expect(data).toHaveProperty("status", 201);
    expect(data).toHaveProperty("userId", post.userId);
    expect(data).toHaveProperty("title", post.title);
});

test("Put Http", async () => {
    const put  = {title:"foo updated title", body:"bar updated body", userId:1, id:1};
    const data: PostsResponse = await http.put<PostsResponse>(`https://jsonplaceholder.typicode.com/posts/${put.id}`, put);

    expect(data).toHaveProperty("status", 200);
    expect(data).toHaveProperty("userId", put.userId);
    expect(data).toHaveProperty("title", put.title);
});

test("Delete Http", async () => {
    const id: number = 1;
    const data: PostsResponse = await http.delete<PostsResponse>(`https://jsonplaceholder.typicode.com/posts/${id}`);

    expect(data).toHaveProperty("status", 200);
});
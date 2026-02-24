
export interface BaseResponse {
    status: 200 | 201 | 400 | 404 | 500;
    error?: string;
}

export interface LogoutResponse extends BaseResponse{
    username?: string;
}

export interface LoginResponse extends BaseResponse{
    username?: string;
    jwt?: string;
}
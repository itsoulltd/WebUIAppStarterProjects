
export interface BaseResponse {
    status: number;
    error?: string;
    reason?: string;
    message?: string;
}

export interface LogoutResponse extends BaseResponse{
    username?: string;
}

export interface LoginResponse extends BaseResponse{
    username?: string;
    jwt?: string;
}
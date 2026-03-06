import { LoginResponse, LogoutResponse } from "../HttpRequest/Response";
import { Threads } from "../Utils/Threads";

function useAuth() {

    async function doLogin(username: string, password: string) : Promise<LoginResponse> {
        //TODO:
        console.log("Login Action =>", `Login... please wait!`)
        await Threads.sleep(1500);
        console.log("Login Action =>", `Login successful for username:${username}`)
        const response: LoginResponse = {status:200, username:"admin", jwt:"jwt-token"}
        return response
    }

    async function doLogout(username: string | null, jwt: string | null) : Promise<LogoutResponse> {
        //TODO:
        console.log("Logout Action =>", `Logout successful for username:${username}`)
        const response: LogoutResponse = {status:200, username:"admin"}
        return response
    }

    return {doLogin, doLogout}
}

export default useAuth;
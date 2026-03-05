
export interface CookieOption {
    path?: string;
    expires?: string;
    domain?: string;
    sameSite: 'Strict' | 'Lax' | 'None';
}

const defaultOptions = {path: "/", sameSite: 'Strict'} as CookieOption;

export function createCookieOption(expireInDays: number = 7, domain: string | null = null) : CookieOption {
    const expires = new Date();
    expires.setDate(expires.getDate() + expireInDays); // expire in x days
    const updatedOptions = {...defaultOptions, expires: expires.toUTCString()} as CookieOption;
    return (domain === null || domain === undefined) ? updatedOptions : {...updatedOptions, domain: domain} as CookieOption;
}

export const Cookies = {
    get: (key: string) : string | null => read(key),
    set: (key: string, value: string, expireInDays: number = 7, domain: string | null = null) : void =>
        write(key, value, createCookieOption(expireInDays, domain)),
    remove: (key: string, domain: string | null = null) : void => remove(key, domain),
}

function read(key: string) :  string | null {
    const cookies = document.cookie.split("; ");
    //console.log(cookies); //TEST
    for (let cookie of cookies) {
        const [inKey, inValue] = cookie.split("=");
        if (inKey === key) return decodeURIComponent(inValue);
    }
    return null;
}

function write(key: string, value: string, options: CookieOption) :  void {
    //e.g. document.cookie = "username=john123; expires=Wed, 11 Mar 2026 10:00:00 GMT; path=/; SameSite=Strict; Secure";
    const optionStr = (options.domain === null || options.domain === undefined)
        ? `expires=${options.expires}; path=${options.path}; SameSite=${options.sameSite}; Secure`
        : `expires=${options.expires}; domain=${options.domain}; path=${options.path}; SameSite=${options.sameSite}; Secure`;

    //console.log(`${key}=${encodeURIComponent(value)}; ${optionStr}`); //TEST
    document.cookie = `${key}=${encodeURIComponent(value)}; ${optionStr}`;
}

function remove(key: string, domain: string | null = null) :  void {
    const options = createCookieOption(-1, domain);
    const optionStr = (options.domain === null || options.domain === undefined)
        ? `expires=${options.expires}; path=${options.path}; SameSite=${options.sameSite}; Secure`
        : `expires=${options.expires}; domain=${options.domain}; path=${options.path}; SameSite=${options.sameSite}; Secure`;

    //console.log(`${key}=; ${optionStr}`); //TEST
    document.cookie = `${key}=; ${optionStr}`;
}
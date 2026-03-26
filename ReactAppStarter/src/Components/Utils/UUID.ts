
export const UUID = {
    randomUUID: (): string  => uniqueID(),
    timestampUUID: (prefix : string = "uuid", seperator: string = "-"): string => timestampID(prefix, seperator),
    cryptoUUID: (): string => crypto.randomUUID(),
}

function uniqueID(): string {
    // ChatGPT gen-code:
    return "xxxxxxxx-xxxx-4xxx-yxxx-xxxxxxxxxxxx".replace(/[xy]/g, c => {
        const r = Math.random() * 16 | 0;
        const v = c === "x" ? r : (r & 0x3 | 0x8);
        return v.toString(16);
    });
}

function timestampID(prefix : string, seperator: string = "-"): string {
    const timestamp = Date.now();
    const random_str = uniqueID().slice(0, 8);
    return `${prefix}${seperator}${random_str}${seperator}${timestamp}`;
}
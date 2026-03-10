
export const Threads = {
    sleep: (ms: number = 1000) => sleep(ms),
}

function sleep(ms: number) : Promise<void> {
    //Simulating: Wait for ms milliseconds before continuing.
    return new Promise((resolve) => setTimeout(resolve, ms));
}
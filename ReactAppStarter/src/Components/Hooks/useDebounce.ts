import { useEffect, useState } from "react";

/**
 * Debouncing in React is a technique used to delay executing a function until a certain amount of time has passed since the last event.
 * It is mainly used to improve performance and avoid unnecessary function calls.
 *
 * Execute a function after a specified delay (in milliseconds).
 * It is part of the Web APIs and works with the JavaScript event loop.
 * Call Stack
 *    ↓
 * setTimeout → Web API (timer)
 *    ↓
 * Callback Queue
 *    ↓
 * Event Loop
 *    ↓
 * Call Stack executes function
 * @param value
 * @param delay
 */
function useDebounce(value: any, delay: number = 500) : any {
    const [debounceValue, setDebounceValue] = useState<any>(value);

    useEffect(() => {
        const timer = setTimeout(() => {
            setDebounceValue(value);
        }, delay);
        return () => clearTimeout(timer);
    }, [value, delay]);

    return debounceValue;
}

export default useDebounce;
import axios from "axios";
import { getToken, removeToken, removeUser } from "../utils/token";

export const authClient = axios.create({
  baseURL: import.meta.env.VITE_API_SERVICE_AUTH,
  headers: {
    "Content-Type": "application/json",
  },
  timeout: 10000,
});

const attachAuthInterceptor = (client: typeof authClient) => {
    client.interceptors.request.use(
        (config) => {
            const token = getToken();

            if (token) {
                config.headers.Authorization = `Bearer ${token}`;
            }

            return config;
        },
        (error) => Promise.reject(error)
    );
};

attachAuthInterceptor(authClient);
authClient.interceptors.request.use((config) => {

    const token = getToken();

    if (token) {
        config.headers.Authorization = `Bearer ${token}`;
    }

    return config;
});

const attachResponseInterceptor = (client: typeof authClient) => {
    client.interceptors.response.use(
        (response) => response,
        (error) => {
            if (error.response?.status === 401) {
                removeToken();
                removeUser();

                window.location.href = "/login";
            }

            return Promise.reject(error);
        }
    );
};

export const scanClient = axios.create({
  baseURL: import.meta.env.VITE_API_SERVICE_SCAN,
  headers: {
    "Content-Type": "application/json",
  },
  timeout: 10000,
});

attachAuthInterceptor(scanClient);
attachResponseInterceptor(scanClient);
scanClient.interceptors.request.use((config) => {

    const token = getToken();

    if (token) {
        config.headers.Authorization = `Bearer ${token}`;
    }

    return config;
});
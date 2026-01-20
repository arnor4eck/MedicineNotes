import axios, {type AxiosError} from 'axios';
import { authService } from './authService.ts';

const API_BASE_URL = 'http://localhost:8080/api';

const apiClient = axios.create({
    baseURL: API_BASE_URL,
    headers: {
        'Content-Type': 'application/json',
    },
});

// добавление токена
apiClient.interceptors.request.use((config) => {
    const token = authService.getToken();

    if (token)
        config.headers.Authorization = `Bearer ${token}`;
    else
        console.error("Нет токена авторизации")

    return config;
},
(error: AxiosError) => {
    console.error('Ошибка запроса:', error);
    return Promise.reject(error);
});

// обработка ошибок
apiClient.interceptors.response.use(
    (response) => response,
    (error) => {
        if (error.response?.status === 401) {
            window.location.href = '/login';
        }
        return Promise.reject(error);
    }
);

export default apiClient;
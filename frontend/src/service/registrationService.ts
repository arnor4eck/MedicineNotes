import type {CreateUserRequest} from "../types/types.ts";
import api from "./api.ts";

const TEMPLATE_URL = "/v1/user/registration"

class RegistrationService{
    async newUser(request : CreateUserRequest): Promise<void> {
        try {
            await api.post(TEMPLATE_URL, request);
        } catch (error) {
            console.error('Ошибка регистрации: ', error);
            throw error;
        }
    }
}

export const registrationService = new RegistrationService();
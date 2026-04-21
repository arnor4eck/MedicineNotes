import type {CreateUserRequest, VerifyCodeRequest, NewCodeRequest} from "../types/types.ts";
import api from "./api.ts";

const TEMPLATE_URL = "/v1/user/registration"

class RegistrationService{
    async preregistration(request : CreateUserRequest): Promise<void> {
        try {
            await api.post(TEMPLATE_URL, request);
        } catch (error) {
            console.error('Ошибка пререгистрации: ', error);
            throw error;
        }
    }

    async registration(request : VerifyCodeRequest) : Promise<boolean> {
        try{
            console.log(TEMPLATE_URL + "/verify");
            const response = await api.post(TEMPLATE_URL + "/verify", request);

            return response.status === 200;
        }catch (error) {
            console.error("Ошибка регистрации: ", error);
            throw error;
        }
    }

    async newCode(request : NewCodeRequest) : Promise<boolean> {
        try{
            const response = await api.post(TEMPLATE_URL + "/new/code", request);

            if(response.status === 200)
                return true;
            else
                throw new Error(response.data.message);

        }catch (error) {
            console.error("Ошибка отправки нового кода: ", error);
            throw error;
        }
    }
}

export const registrationService = new RegistrationService();
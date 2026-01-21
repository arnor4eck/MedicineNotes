import api from "./api.ts";
import type {Intake} from "../types/types.ts";

const INTAKE_URL = "/v1/intake"

class IntakeService {
    async getAllUserIntakes(date? : string): Promise<Intake[]> {
        try {
            const url = `${INTAKE_URL}/my${date ? `?date=${date}` : ''}`;

            const res = await api.get<Intake[]>(url);
            return res.data;
        } catch (error) {
            console.error('Ошибка получения всех приёмов: ', error);
            throw error;
        }
    }

    async changeIntakeStatus(id : number,
                             status : string): Promise<Intake> {
        try {
            const request = {
                status: status
            }

            const res = await api.patch<Intake>(INTAKE_URL + `/${id}/status`, request);
            return res.data;
        } catch (error) {
            console.error(`Ошибка изменения статуса приёма c id ${id}:` , error);
            throw error;
        }
    }

    async getMedicineIntakeById(id: number): Promise<Intake> {
        try {
            const res = await api.get<Intake>(INTAKE_URL + `/${id}`);
            return res.data;
        } catch (error) {
            console.error(`Ошибка получения приёма с id ${id}: `, error);
            throw error;
        }
    }
}

export const intakeService = new IntakeService();
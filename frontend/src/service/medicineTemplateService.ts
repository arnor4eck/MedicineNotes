import api from "./api.ts";
import type {MedicineTemplate} from "../types/types.ts";

const TEMPLATE_URL = "/v1/template"

class MedicineTemplateService {
    async getAllUserTemplates(): Promise<MedicineTemplate[]> {
        try {
            const res = await api.get<MedicineTemplate[]>(TEMPLATE_URL + "/my");
            return res.data;
        } catch (error) {
            console.error('Ошибка получения всех шаблонов: ', error);
            throw error;
        }
    }

    async createMedicineTemplate(request : Omit<MedicineTemplate, 'id'>): Promise<MedicineTemplate> {
        try {
            const res = await api.post<MedicineTemplate>(TEMPLATE_URL + "/create", request);
            return res.data;
        } catch (error) {
            console.error('Ошибка создания шаблона: ', error);
            throw error;
        }
    }

    async getMedicineTemplateById(id: string): Promise<MedicineTemplate> {
        try {
            const res = await api.get<MedicineTemplate>(TEMPLATE_URL + `/${id}`);
            return res.data;
        } catch (error) {
            console.error(`Ошибка получения шаблона с id  ${id}: `, error);
            throw error;
        }
    }
}

export const medicineTemplateService = new MedicineTemplateService();
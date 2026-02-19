import type {FullTemplateStatistics, IntakeStatisticsResponse} from "../types/types.ts";
import api from "./api.ts";


const STATISTICS_URL = "/v1/statistics";

class StatisticsService {
    async getTemplateStatisticsByIdAndOffset(id : string,
                                             offset : number): Promise<FullTemplateStatistics> {
        try{
            const res = await api.get<FullTemplateStatistics>(STATISTICS_URL + `/templates/${id}?offset=${offset}`);
            return res.data;
        }catch (error) {
            console.error(`Ошибка получения статистики шаблона с id: ${id}`);
            throw error;
        }
    }

    async getIntakeStatisticsByDate(date : string | null): Promise<IntakeStatisticsResponse> {
        try{
            const res = await api.get<IntakeStatisticsResponse>(STATISTICS_URL + `/intakes?date=${date}`);
            return res.data;
        }catch (error) {
            console.error(`Ошибка получения статистики по дате: ${date}`);
            throw error;
        }
    }
}

export const statisticsService = new StatisticsService();
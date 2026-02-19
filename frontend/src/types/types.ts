export interface User{
    id: number;
    username: string;
    email: string;
}

export interface MedicineTemplate {
    id: number;
    name: string;
    description: string;
    quantityPerDay: number;
    until: string;
}

export interface Intake {
    id: number;
    name: string;
    adoptedIn: string;
    status: 'PENDING' | 'DONE' | 'SKIPPED';
    shouldAdoptedIn: string;
}

export interface CreateTemplateRequest {
    name: string;
    description: string;
    timesPerDay: number;
    endDate: string;
}

export interface CreateUserRequest {
    username: string;
    email: string;
    password: string;
}

export interface AuthenticationRequest {
    email: string;
    password: string;
}

export interface AuthenticationResponse {
    token: string;
}

// Статистика

export interface TemplateStatisticsUnit {
    shouldAdoptedIn: string;
    doneCount: number;
}

export interface FullTemplateStatistics {
    templateStatistics: TemplateStatisticsUnit[];
    maxTimesPerDay: number;
}

// Статистика по датам

// Единица статистики
export interface StatusAndCountUnit {
    status: 'PENDING' | 'DONE' | 'SKIPPED';
    count: number;
}

export interface IntakeStatistics {
    units: StatusAndCountUnit[];
}

// Общая статистика по датам
export interface StatisticsByDate {
    [key: string]: IntakeStatistics;  // ключ - название приема
}

// Корневой интерфейс
export interface IntakeStatisticsResponse {
    statisticsByDate: StatisticsByDate;
}
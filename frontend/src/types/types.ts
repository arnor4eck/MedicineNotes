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
class StatusService{
    getStatusInfo (status : string) : string {
        switch (status) {
            case 'PENDING':
                return 'Ожидание';
            case 'DONE':
                return 'Принято';
            case 'SKIPPED':
                return 'Пропущено';
            default:
                return 'Неизвестно';
        }
    };
}

export const statusService = new StatusService();
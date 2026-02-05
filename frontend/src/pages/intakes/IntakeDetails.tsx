import {useEffect, useState} from "react";
import Header from "../../components/header/Header.tsx";
import {useParams} from "react-router-dom";
import './IntakeDetails.css'
import {intakeService} from "../../service/intakeService.js";
import type {ApiError} from "../../types/apiError.ts";
import type {Intake} from "../../types/types.ts";
import {statusService} from "../../service/statusService.ts";

export default function TemplateDetail(){
    const [loading, setLoading] = useState(true);
    const [intake, setIntake] = useState<Intake>({} as Intake);
    const [error, setError] = useState('');
    const { id } = useParams();

    useEffect(() => {
        const fetchData = async () => {
            try {
                if(!id)
                    return;

                setLoading(true);
                const intakeData = await intakeService.getMedicineIntakeById(id);

                setIntake(intakeData);

            } catch (error) {
                setError((error as ApiError).messages.join('\n'));
            } finally {
                setLoading(false);
            }
        };

        fetchData();
    }, [id]);

    const handleDone = async () => {
        try {
            if(!id)
                return;

            setLoading(true);
            setError('');
            const newRes = await intakeService.changeIntakeStatus(id, 'DONE');

            setIntake(newRes)
        } catch (err) {
            console.error('Ошибка обновления статуса:', err);
            setError('Не удалось обновить статус приёма');
        } finally {
            setLoading(false);
        }
    };


    return (
        <div className='bg'>
            <Header />

            {error && (
                <div className="error-message">{error}</div>
            )}

            {loading ? (
                <div className='loading'>
                    <p>Загрузка приёма...</p>
                </div>
            ) : (
                <div className='center_container'>
                    <div className='intake_card'>
                        <div className="intake_card__header">
                            <h3 className="intake_card__name">{intake.name}</h3>
                        </div>

                        {statusService.getStatusInfo(intake.status) === 'Ожидание' && <div className='intake_card--btn-cnt'>
                            <button className='card__button card__button--primary' onClick={handleDone}>Поставить отметку о приёме</button>
                        </div>}

                        <div className="intake_card__info">
                            <div className="intake_card__info-item">
                                <span className="intake_card__info-label">Время приёма</span>
                                <span className='intake_card__info-value '>
                                        {intake.adoptedIn ?? "Не принято"}
                                    </span>
                            </div>

                            <div className="intake_card__info-item">
                                <span className="intake_card__info-label">Должно быть принято</span>
                                <span className='intake_card__info-value '>
                                        {intake.shouldAdoptedIn}
                                    </span>
                            </div>

                            <div className="intake_card__info-item">
                                <span className="intake_card__info-label">Статус</span>
                                <span className="intake_card__info-value">
                                        {statusService.getStatusInfo(intake.status)}
                                    </span>
                            </div>
                        </div>
                    </div>
                </div>
            )}
        </div>
    )
}
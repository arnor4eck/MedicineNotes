import {useNavigate, useSearchParams} from "react-router-dom";
import {intakeService} from "../../service/intakeService.js";
import {useEffect, useState} from "react";
import Header from "../../components/header/Header.tsx";
import './Intakes.css'
import type {Intake} from "../../types/types.ts";
import IntakeCard from "../../components/intake/card/IntakeCard.tsx";
import type {ApiError} from "../../types/apiError.ts";

export default function Intakes() {
    const [searchParams] = useSearchParams()
    const [loading, setLoading] = useState(true);
    const [intakes, setIntakes] = useState<Intake[]>([]);
    const [error, setError] = useState('');
    const navigate = useNavigate();

    useEffect(() => {
        const fetchData = async () => {
            try {
                const date = searchParams.get('date');

                setLoading(true);

                const intakesData = await intakeService.getAllUserIntakes(date);

                setIntakes(intakesData);
                setError('');
            } catch (error) {
                console.error('Ошибка загрузки:', error);
                setError((error as ApiError).messages.join('\n'));
            } finally {
                setLoading(false);
            }
        };

        fetchData();
    }, []);

    const getContainerClass = () => {
        if (intakes.length <= 2) {
            return 'templates_container center-items';
        }
        return 'templates_container';
    };

    return (
        <div className='bg'>
            <Header />

            <div className='templates__bg'>
                {error && (
                    <div className="error-message">{error}</div>
                )}

                {loading ? (
                    <div className='loading'>
                        <p>Загрузка приёмов...</p>
                    </div>
                ) : intakes.length === 0 ? (
                    <div className="templates_container center-items">
                        <div className="empty-state">
                            <h3>Приёмов пока нет</h3>
                            <p>Создайте свой первый шаблон лекарств</p>
                            <button
                                className="create-btn"
                                onClick={() => navigate('/templates/create')}
                            >
                                + Создать шаблон
                            </button>
                        </div>
                    </div>
                ) : (
                    <ul className={getContainerClass()}>
                        {intakes.map((intake) => (
                            <li key={intake.id}>
                                <IntakeCard date={intake.shouldAdoptedIn} name={intake.name} status={intake.status} id={intake.id} />
                            </li>
                        ))}
                    </ul>
                )}
            </div>
        </div>
    );
};
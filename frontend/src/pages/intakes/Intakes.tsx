import {intakeService} from "../../service/intakeService.js";
import {useEffect, useState} from "react";
import Header from "../../components/header/Header.tsx";
import './Intakes.css'
import type {Intake} from "../../types/types.ts";
import IntakeCard from "../../components/intake/card/IntakeCard.tsx";
import type {ApiError} from "../../types/apiError.ts";
import {dateService} from "../../service/dateService.ts";

export default function Intakes() {
    const [dateParam, setDateParam] = useState(dateService.formatDate(new Date()));
    const [loading, setLoading] = useState(true);
    const [intakes, setIntakes] = useState<Intake[]>([]);
    const [filteredIntakes, setFilteredIntakes] = useState<Intake[]>(intakes);
    const [error, setError] = useState('');

    useEffect(() => {
        const fetchData = async () => {
            try {
                setLoading(true);
                console.log(dateParam);
                const intakesData = await intakeService.getAllUserIntakes(dateParam);

                setIntakes(intakesData);
                setFilteredIntakes(intakesData);
                setError('');
            } catch (error) {
                console.error('Ошибка загрузки:', error);
                setError((error as ApiError).messages.join('\n'));
            } finally {
                setLoading(false);
            }
        };

        fetchData();
    }, [dateParam]);

    const filterIntakesByStatus = (status : string) => {
        if(status == 'ALL')
            setFilteredIntakes(intakes);
        else
            setFilteredIntakes(intakes.filter((intake) => {
                if(intake.status === status)
                    return intake;
            }));
    }

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

                <div className="upper__content">
                    <div className="group">
                        <h3>Дата приёмов</h3>
                        <input
                            type="date"
                            id="date"
                            value={dateParam}
                            onChange={(e) => setDateParam(e.target.value)}
                            required
                            disabled={loading}
                        />
                    </div>
                    <div className="group">
                        <h3>Статус</h3>

                        <select
                            id="filter-type"
                            onChange={(e) => filterIntakesByStatus(e.target.value)}
                            disabled={loading}
                        >
                            <option value="ALL">Все</option>
                            <option value="PENDING">Ожидание</option>
                            <option value="DONE">Принято</option>
                            <option value="SKIPPED">Пропущено</option>
                        </select>
                    </div>
                </div>

                {loading ? (
                    <div className='loading'>
                        <p>Загрузка приёмов...</p>
                    </div>
                ) : filteredIntakes.length === 0 ? (
                    <div className="templates_container center-items">
                        <div className="empty-state">
                            <h3>Приёмы с заданными параметрами отсутствуют</h3>
                        </div>
                    </div>
                ) : (
                    <ul className={getContainerClass()}>
                        {filteredIntakes.map((intake) => (
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
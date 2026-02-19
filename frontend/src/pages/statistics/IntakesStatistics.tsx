import {useEffect, useState} from "react";
import {statisticsService} from "../../service/statisticsService.ts";
import Header from "../../components/header/Header.tsx";
import './IntakesStatistics.css'
import {dateService} from "../../service/dateService.ts";
import type {ApiError} from "../../types/apiError.ts";
import type {IntakeStatisticsResponse} from "../../types/types.ts";
import IntakeStatisticsCard from "../../components/intake/statistics_card/IntakeStatisticsCard.tsx";

export default function IntakesStatistics() {
    const [error, setError] = useState('');
    const [loading, setLoading] = useState(true);
    const [date, setDate] = useState<Date>(new Date());
    const [statisticsIntakes, setStatisticsIntakes] = useState<IntakeStatisticsResponse>({} as IntakeStatisticsResponse);

    useEffect(() => {
        const fetchData = async () =>{
            try{
                setLoading(true);
                const statisticsData =
                    await statisticsService.getIntakeStatisticsByDate(dateService.formatDate(date));
                console.log(statisticsData);
                setStatisticsIntakes(statisticsData);

            } catch (error){
                setError((error as ApiError).messages.join('\n'));
            } finally {
                setLoading(false);
            }
        }

        fetchData();
    }, [date]);

    return(
        <div className='bg'>
            <Header />

            {error && (
                <div className="error-message">{error}</div>
            )}

            {loading ? (
                <div className='loading'>
                    <p>Загрузка...</p>
                </div>
            ) : (
                <div className='statistics__bg'>
                    <div className="date-buttons">
                        <button
                            className="date-buttons__button date-buttons__button__left"
                            onClick={() => {
                                const newDate = new Date(date);
                                newDate.setDate(newDate.getDate() - 1);
                                setDate(newDate);
                            }}>
                            <b>&lt;</b>
                        </button>

                        <div className="date-buttons__date">
                            {dateService.formatDate(date)}
                        </div>

                        <button
                            className="date-buttons__button date-buttons__button__right"
                            onClick={() => {
                                const newDate = new Date(date);
                                newDate.setDate(newDate.getDate() + 1);
                                setDate(newDate);
                            }}>
                            <b>&gt;</b>
                        </button>
                    </div>
                    <div className="intakes">
                        {Object.entries(statisticsIntakes.statisticsByDate).map(([templateName, stats]) => (
                            <IntakeStatisticsCard name={templateName} units={stats.units} />
                        ))}
                    </div>
                </div>

            )}
        </div>
    );
}
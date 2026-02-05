import {useParams} from "react-router-dom";
import {useEffect, useState} from "react";
import type {FullTemplateStatistics} from "../../types/types.ts";
import {statisticsService} from "../../service/statisticsService.ts";
import type {ApiError} from "../../types/apiError.ts";
import Header from "../../components/header/Header.tsx";
import './TemplateStatistics.css'
import {dateService} from "../../service/dateService.ts";
import VerticalProgressBar from "../../components/VerticalProgressBar.tsx";

export default function TemplateStatistics() {
    const { id } = useParams();
    const [error, setError] = useState('');
    const [loading, setLoading] = useState(true);
    const [templateStatistics, setTemplateStatistics] = useState<FullTemplateStatistics>({} as FullTemplateStatistics);
    const [offset, setOffset] = useState(0);
    const [startDate, setStartDate] = useState<Date>(new Date());

    useEffect(() => {
        const fetchData = async () =>{
            try{
                if(!id)
                    return;

                setLoading(true);
                const statisticsData = await statisticsService.getTemplateStatisticsByIdAndOffset(id, offset);
                setTemplateStatistics(statisticsData);
                console.log(statisticsData);
            } catch (error){
                setError((error as ApiError).messages.join('\n'));
            } finally {
                setLoading(false);
            }
        }

        fetchData();
    }, [id, offset]);

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
                                if(offset - 1 < 0) {
                                    setError("Нельзя посмотреть дату позже начала приёмов");
                                    return;
                                }else
                                    setError('')

                                setOffset(offset - 1);
                                const newDate = new Date(startDate);
                                newDate.setDate(newDate.getDate() - 5);
                                setStartDate(newDate);
                            }}>
                            <b>&lt;</b>
                        </button>

                        <div className="date-buttons__date">
                            {dateService.formatDate(startDate)} : {dateService.formatDate(new Date(startDate.getTime() + 5 * 24 * 60 * 60 * 1000))}
                        </div>

                        <button
                            className="date-buttons__button date-buttons__button__right"
                            onClick={() => {
                                setOffset(offset + 1);
                                const newDate = new Date(startDate);
                                newDate.setDate(newDate.getDate() + 5);
                                setStartDate(newDate);
                                console.log(offset);
                            }}>
                            <b>&gt;</b>
                        </button>
                    </div>

                    {templateStatistics.templateStatistics.length === 0 ?
                        <div className="empty-state">
                            <p>Статистики по введённым данным не найдено</p>
                        </div> :
                        <div className="statistics__main">
                                {templateStatistics.templateStatistics.map((item, index) => (
                                    <VerticalProgressBar key={index} max={templateStatistics.maxTimesPerDay} done={item.doneCount}
                                                         date = {item.shouldAdoptedIn} height={100} width={50} barColor="#7aaefb" backgroundColor="#aaaaaa" />
                                ))}
                        </div>
                    }
                </div>
            )}
        </div>
    );
}
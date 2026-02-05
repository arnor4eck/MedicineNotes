import {medicineTemplateService} from "../../service/medicineTemplateService.ts";
import {useEffect, useState} from "react";
import Header from "../../components/header/Header.tsx";
import {useNavigate, useParams} from "react-router-dom";
import './TemplateDetail.css'
import type {ApiError} from "../../types/apiError.ts";
import type {MedicineTemplate} from "../../types/types.ts";

export default function TemplateDetail(){
    const [loading, setLoading] = useState<boolean>(true);
    const [template, setTemplate] = useState<MedicineTemplate>({} as MedicineTemplate);
    const [error, setError] = useState<string>('');
    const navigate = useNavigate();
    const { id } = useParams<{ id: string }>();

    useEffect(() => {
        const fetchData = async () => {
            try {
                if(!id)
                    return;

                setLoading(true);
                const templateData = await medicineTemplateService.getMedicineTemplateById(id);

                setTemplate(templateData);

            } catch (error) {
                setError((error as ApiError).messages.join('\n'));
            } finally {
                setLoading(false);
            }
        };

        fetchData();
    }, [id]);

    const isActive = new Date().setHours(0, 0, 0, 0) < new Date(template.until).setHours(0, 0, 0, 0);

    return (
        <div className='bg'>
            <Header />

            {error && (
                <div className="error-message">{error}</div>
            )}

            {loading ? (
                    <div className='loading'>
                        <p>Загрузка шаблонов...</p>
                    </div>
                ) : (
                    <div className='center_container'>
                        <div className='template_card'>
                            <div className="template_card__header">
                                <h3 className="template_card__name">{template.name}</h3>
                            </div>

                            <div className="template_card__description">
                                <p>{template.description}</p>
                            </div>

                            <div className="template_card__info">
                                <div className="template_card__info-item">
                                    <span className="template_card__info-label">До</span>
                                    <span className={'template_card__info-value ' + (isActive ? '' : 'template_card__info-data--red')}>
                                        {template.until}
                                    </span>
                                </div>

                                <div className="template_card__info-item">
                                    <span className="template_card__info-label">В день</span>
                                    <span className="template_card__info-value">
                                        {template.quantityPerDay} р.
                                    </span>
                                </div>
                            </div>
                            <div className="template_card__buttons">
                                <button style={{
                                    width: `70%`,
                                    marginBottom: `15px`
                                }} className="card__button card__button--primary" onClick={() => navigate(`/statistics/templates/${id}`)}>Статистика</button>
                            </div>
                        </div>
                    </div>
            )}
        </div>
    )
}
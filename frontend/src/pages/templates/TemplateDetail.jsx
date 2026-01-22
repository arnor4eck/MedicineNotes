import {medicineTemplateService} from "../../service/medicineTemplateService.js";
import {useEffect, useState} from "react";
import Header from "../../components/header/Header.jsx";
import {useParams} from "react-router-dom";
import './TemplateDetail.css'

export default function TemplateDetail(){
    const [loading, setLoading] = useState(true);
    const [template, setTemplate] = useState({});
    const [error, setError] = useState('');
    const { id } = useParams();

    useEffect(() => {
        const fetchData = async () => {
            try {
                setLoading(true);
                const templateData = await medicineTemplateService.getMedicineTemplateById(id);

                setTemplate(templateData);

            } catch (error) {
                setError(error.messages);
            } finally {
                setLoading(false);
            }
        };

        fetchData();
    }, []);

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
                        </div>
                    </div>
            )}
        </div>
    )
}
import { useEffect, useState } from 'react';
import { useNavigate } from 'react-router-dom';
import Header from "../../components/header/Header.jsx";
import { medicineTemplateService } from "../../service/medicineTemplateService.js";
import './TemplatesPage.css';
import TemplateCard from "../../components/template/card/TemplateCard.jsx";

const TemplatesPage = () => {
    const [loading, setLoading] = useState(true);
    const [templates, setTemplates] = useState([]);
    const [error, setError] = useState('');
    const navigate = useNavigate();

    useEffect(() => {
        const fetchData = async () => {
            try {
                setLoading(true);
                const projectsData = await medicineTemplateService.getAllUserTemplates();
                setTemplates(projectsData);
                setError('');
            } catch (error) {
                console.error('Ошибка загрузки:', error);
                setError('Не удалось загрузить шаблоны');
            } finally {
                setLoading(false);
            }
        };

        fetchData();
    }, []);

    const getContainerClass = () => {
        if (templates.length <= 2) {
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
                        <p>Загрузка шаблонов...</p>
                    </div>
                ) : templates.length === 0 ? (
                    <div className="templates_container center-items">
                        <div className="empty-state">
                            <h3>Шаблонов пока нет</h3>
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
                    <div>
                    <ul className={getContainerClass()}>
                        {templates.map((template) => (
                            <li key={template.id}>
                                <TemplateCard
                                    id={template.id}
                                    name={template.name}
                                    until={template.until}
                                />
                            </li>
                        ))}
                    </ul>
                    <div className="center-items">
                            <button
                                className="create-btn"
                                onClick={() => navigate('/templates/create')}>
                            + Создать шаблон
                            </button>
                    </div>
                </div>
                )}
            </div>
        </div>
    );
};

export default TemplatesPage;
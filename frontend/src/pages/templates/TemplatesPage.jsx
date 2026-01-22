import {useEffect, useState} from 'react';
import { useNavigate } from 'react-router-dom';
import Header from "../../components/header/Header.jsx";
import {medicineTemplateService} from "../../service/medicineTemplateService.js";
import './TemplatesPage.css'
import TemplateCard from "../../components/template/card/TemplateCard.jsx";

const TemplatesPage = () => {
    const [loading, setLoading] = useState(false);
    const [templates, setTemplates] = useState([]);
    const [error, setError] = useState('');
    const navigate = useNavigate();

    useEffect(() => {
        const fetchData = async () => {
            try {
                setLoading(true);
                const projectsData = await medicineTemplateService.getAllUserTemplates();

                console.log(projectsData);
                setTemplates(projectsData);

            } catch (error) {
                setError(error);
            } finally {
                setLoading(false);
            }
        };

        fetchData();
    }, []);

    return (
        <div className='bg templates__bg'>
            <Header />
            {loading ?
                <div className='loading'>Загрузка...</div>
                :
                <ul className='templates_container'>
                    {templates.map((template) => (
                        <li key={template.id}>
                            <TemplateCard name={template.name} until={template.until} />
                        </li>
                    ))}
                </ul>
            }
        </div>
    );
};

export default TemplatesPage;
import { useState } from 'react';
import { useNavigate } from 'react-router-dom';
import './CreateTemplate.css'
import '../css/form.css'
import {medicineTemplateService} from "../../service/medicineTemplateService.js";
import type {ApiError} from "../../types/apiError.ts";

const CreateTemplatePage = () => {
    const [name, setName] = useState('');
    const [description, setDescription] = useState('');
    const [quantityPerDay, setQuantityPerDay] = useState(1);
    const [until, setUntil] = useState('');
    const [error, setError] = useState('');
    const [loading, setLoading] = useState(false);
    const navigate = useNavigate();

    const handleSubmit = async (e: React.FormEvent) => {
        e.preventDefault();
        setLoading(true);
        setError('');

        try {
            await medicineTemplateService.createMedicineTemplate({
                name, description, quantityPerDay, until
            });
            setTimeout(() => {
                navigate('/templates', { replace: true });
            }, 100);

        } catch (error) {
            setError((error as ApiError).messages.join('\n'));
        } finally {
            setLoading(false);
        }
    };

    return (
        <div className='bg bg_create'>
            <form onSubmit={handleSubmit} className="form">
                <p>Создание шаблона приёма медицинского препарата</p>

                {error && <div className="error-message">{error}</div>}

                <div className="form-group">
                    <label htmlFor="name">Название:</label>
                    <input
                        type="text"
                        id="name"
                        value={name}
                        onChange={(e) => setName(e.target.value)}
                        required
                        disabled={loading}
                        placeholder="Введите название"
                    />
                </div>

                <div className="form-group">
                    <label htmlFor="description">Описание:</label>
                    <input
                        type="text"
                        id="description"
                        value={description}
                        onChange={(e) => setDescription(e.target.value)}
                        required
                        disabled={loading}
                        placeholder="Введите описание"
                    />
                </div>

                <div className="form-group">
                    <label htmlFor="quantity">Количество:</label>
                    <input
                        type="number"
                        id="quantity"
                        value={quantityPerDay}
                        onChange={(e) => setQuantityPerDay(Number(e.target.value))}
                        required
                        disabled={loading}
                    />
                </div>

                <div className="form-group">
                    <label htmlFor="date">Конец:</label>
                    <input
                        type="date"
                        id="date"
                        value={until}
                        onChange={(e) => setUntil(e.target.value)}
                        required
                        disabled={loading}
                    />
                </div>

                <button
                    type="submit"
                    disabled={loading}
                    className="main-button"
                >
                    {loading ? 'Создание...' : 'Создать'}
                </button>
            </form>
        </div>
    );
};

export default CreateTemplatePage;
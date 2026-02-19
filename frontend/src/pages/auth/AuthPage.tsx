import { useState } from 'react';
import { useNavigate } from 'react-router-dom';
import { authService } from "../../service/authService.ts";
import './AuthPage.css'
import '../css/form.css'
import type {ApiError} from "../../types/apiError.ts";

export default function AuthPage(){
    const [email, setEmail] = useState('');
    const [password, setPassword] = useState('');
    const [error, setError] = useState('');
    const [loading, setLoading] = useState(false);
    const navigate = useNavigate();

    const handleSubmit = async (e: React.FormEvent) => {
        e.preventDefault();
        setLoading(true);
        setError('');

        try {
            await authService.login({ email, password });
            setTimeout(() => {
                navigate('/templates', { replace: true });
            }, 100);

        } catch (error) {
            setError((error as ApiError).messages.join(','));
        } finally {
            setLoading(false);
        }
    };

    return (
        <div className='bg bg_auth'>
            <form onSubmit={handleSubmit} className="form">
                <p>Вход в систему</p>

                {error && <div className="error-message">{error}</div>}

                <div className="form-group">
                    <label htmlFor="email">Email:</label>
                    <input
                        type="email"
                        id="email"
                        value={email}
                        onChange={(e) => setEmail(e.target.value)}
                        required
                        disabled={loading}
                        placeholder="Введите ваш email"
                    />
                </div>

                <div className="form-group">
                    <label htmlFor="password">Пароль:</label>
                    <input
                        type="password"
                        id="password"
                        value={password}
                        onChange={(e) => setPassword(e.target.value)}
                        required
                        disabled={loading}
                        placeholder="Введите ваш пароль"
                    />
                </div>

                <button
                    type="submit"
                    disabled={loading}
                    className="main-button"
                >
                    {loading ? 'Вход...' : 'Войти'}
                </button>
            </form>
        </div>
    );
};
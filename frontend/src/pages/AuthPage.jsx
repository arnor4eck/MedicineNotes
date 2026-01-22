import { useState } from 'react';
import { useNavigate } from 'react-router-dom';
import { authService } from "../service/authService.ts";
import './AuthPage.css'

const AuthPage = () => {
    const [email, setEmail] = useState('');
    const [password, setPassword] = useState('');
    const [error, setError] = useState('');
    const [loading, setLoading] = useState(false);
    const navigate = useNavigate();

    const handleSubmit = async (e) => {
        e.preventDefault();
        setLoading(true);
        setError('');

        try {
            const response = await authService.login({ email, password });
            setTimeout(() => {
                console.log('Navigating');
                navigate('/templates', { replace: true });
            }, 100);

        } catch (error) {
            setError(error.messages);
        } finally {
            setLoading(false);
        }
    };

    return (
        <div className='bg'>
            <form onSubmit={handleSubmit} className="login-form">
                <p>Вход в систему</p>

                {error && <div className="error-message">{error}</div>}

                <div className="authGroup">
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

                <div className="authGroup">
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
                    className="authEnterBtn"
                >
                    {loading ? 'Вход...' : 'Войти'}
                </button>
            </form>
        </div>
    );
};

export default AuthPage;
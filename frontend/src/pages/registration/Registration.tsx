import { useState } from 'react';
import { useNavigate, Link } from 'react-router-dom';
import { registrationService } from "../../service/registrationService.ts";
import '../css/form.css'
import './Registration.css'
import type {ApiError} from "../../types/apiError.ts";

export default function Registration(){
    const [email, setEmail] = useState('');
    const [username, setUserName] = useState('');
    const [password, setPassword] = useState('');
    const [repeatPassword, setRepeatPassword] = useState('');
    const [error, setError] = useState('');
    const [loading, setLoading] = useState(false);
    const navigate = useNavigate();

    const handleSubmit = async (e: React.FormEvent) => {
        e.preventDefault();
        setLoading(true);
        setError('');

        if(password !== repeatPassword){
            setError('Введённые пароли не совпадают');
            setLoading(false);
            return;
        }

        try {
            await registrationService.newUser({username, email, password});
            setTimeout(() => {
                navigate('/auth', { replace: true });
            }, 100);

        } catch (error) {
            setError((error as ApiError).messages.join(','));
        } finally {
            setLoading(false);
        }
    };

    return (
        <div className='bg bg_form'>
            <form onSubmit={handleSubmit} className="form">
                <p>Вход в систему</p>

                {error && <div className="error-message">{error}</div>}

                <div className="form-group">
                    <label htmlFor="name">Email:</label>
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
                    <label htmlFor="username">Имя:</label>
                    <input
                        type="text"
                        id="username"
                        value={username}
                        onChange={(e) => setUserName(e.target.value)}
                        required
                        disabled={loading}
                        placeholder="Введите имя пользователя"
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

                <div className="form-group">
                    <label htmlFor="repeat-password">Пароль:</label>
                    <input
                        type="password"
                        id="repeat-password"
                        value={repeatPassword}
                        onChange={(e) => setRepeatPassword(e.target.value)}
                        required
                        disabled={loading}
                        placeholder="Повторите ваш пароль"
                    />
                </div>

                <p className="already" style={{ margin: '0px', fontSize: '16px', textAlign: 'center' }}>
                    Уже зарегистрированы? <Link to="/auth">Войти</Link>
                </p>

                <button
                    type="submit"
                    disabled={loading}
                    className="main-button"
                >
                    {loading ? 'Регистрация...' : 'Зарегистрироваться'}
                </button>
            </form>
        </div>
    );
};
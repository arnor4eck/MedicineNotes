import { useState, useEffect } from 'react';
import { useNavigate } from 'react-router-dom';
import { registrationService } from '../../service/registrationService';
import { useVerificationStore } from '../../storage/verificationStorage.ts';
import '../css/form.css';
import './VerifyEmail.css';
import type {ApiError} from "../../types/apiError.ts";

export default function VerifyEmail() {
    const [code, setCode] = useState('');
    const [error, setError] = useState('');
    const [loading, setLoading] = useState(false);
    const navigate = useNavigate();

    const { email, verificationStarted, resetVerification } = useVerificationStore();

    useEffect(() => {
        if (!verificationStarted || !email) {
            navigate('/registration');
        }
    }, [verificationStarted, email, navigate]);

    const handleVerify = async (e: React.FormEvent) => {
        e.preventDefault();

        setError('');
        if (!email || !code) {
            setError('Нет необходимых полей.');
            return;
        }

        setLoading(true);

        try {
            await registrationService.registration({ email, code });

            navigate('/auth', { replace: true });
            setTimeout(() => {
                resetVerification();
            }, 1000);
        } catch (error) {
            setError((error as ApiError).messages.join(',') || 'Неверный код');
        } finally {
            setLoading(false);
        }
    };

    const handleSendNewCode = async () => {
        setError('');
        if (!email) {
            setError('Нет необходимых полей.');
            return;
        }

        setLoading(true);

        try {
            await registrationService.newCode({ email });

            setError('Новый код отправлен на почту');
        } catch (error) {
            setError((error as ApiError).messages.join(','));
        } finally {
            setLoading(false);
        }
    };

    const handleBackToRegistration = () => {
        resetVerification();
        navigate('/registration');
    };

    return (
    <div className='bg bg_form'>
        <form onSubmit={handleVerify} className="form">
            <p>Подтверждение email</p>

            <p style={{ textAlign: 'center', marginBottom: '0px', fontSize: '14px', color: '#666' }}>
                Код отправлен на {email}
            </p>

            {error && <div className="error-message">{error}</div>}

            <div className="form-group">
                <input
                    type="text"
                    id="code"
                    value={code}
                    onChange={(e) => setCode(e.target.value)}
                    required
                    disabled={loading}
                    placeholder="Введите код из письма"
                    maxLength={5}
                    autoFocus/>
            </div>

            <div className="verify-buttons">
                <button
                    type="button"
                    onClick={handleSendNewCode}
                    disabled={loading}
                    className="resend-button">
                    Отправить код повторно
                </button>

                <button
                    type="button"
                    onClick={handleBackToRegistration}
                    disabled={loading}
                    className="back-button">
                    Назад к регистрации
                </button>
            </div>

            <button type="submit"
                disabled={loading || code.length !== 5}
                className="main-button">
                {loading ? 'Проверка...' : 'Подтвердить'}
            </button>
        </form>
    </div>
    );
}
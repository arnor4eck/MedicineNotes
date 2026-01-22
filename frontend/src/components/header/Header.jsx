import { Link, useLocation } from 'react-router-dom';
import './Header.css'
import {authService} from "../../service/authService.ts";

export default function Header() {
    const location = useLocation();
    const nowDate = new Date();

    const req = `${nowDate.getFullYear()}-${String(nowDate.getMonth() + 1).padStart(2, '0')}-${String(nowDate.getDate()).padStart(2, '0')}`;

    const navItems = [
        { path: '/templates', label: 'Шаблоны' },
        { path: '/intakes?date=' + req, label: 'Сегодняшние приёмы' },
        authService.isAuthenticated() ? { path:  '/logout', label: 'Выйти' }
            : { path: 'auth', label: 'Авторизоваться' }
    ];

    return (
        <div className='center_class'>
        <header className="main-navigation">
            <div className="nav-brand">
                <h2>MedicineNotes</h2>
            </div>

            <ul className="nav-menu">
                {navItems.map((item) => (
                    <li key={item.path}>
                        <Link
                            to={item.path}
                            className={
                                location.pathname === item.path
                                    ? 'nav-link active'
                                    : 'nav-link'
                            }
                        >
                            {item.label}
                        </Link>
                    </li>
                ))}
            </ul>
        </header>
        </div>
    );
}

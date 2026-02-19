import { Link, useLocation } from 'react-router-dom';
import './Header.css'

export default function Header() {
    const location = useLocation();

    const navItems = [
        { path: '/templates', label: 'Шаблоны' },
        { path: '/intakes', label: 'Приёмы' },
        { path:  '/statistics/intakes', label: 'Статистика' },
        { path:  '/logout', label: 'Выйти' }
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

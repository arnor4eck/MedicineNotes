import './IntakeCard.css'
import { useNavigate } from "react-router-dom";

export default function IntakeCard({id, name, status, date}){
    const navigate = useNavigate();

    const getStatusInfo = (status) => {
        switch (status) {
            case 'PENDING':
                return 'Ожидание';
            case 'DONE':
                return 'Принято';
            case 'SKIPPED':
                return 'Пропущено';
            default:
                return 'Неизвестно';
        }
    };

    return(
      <article className='intake-card'>
          <div className='intake-card__header'>
              <p className='intake-card__title'>{name}</p>
          </div>
          <div className='intake-card__bottom'>
              <button onClick={() => navigate(`/intakes/${id}`)}
                      className='intake-card__button intake-card__button--primary'> Подробнее</button>
              <p className='intake-card__date'>{date}</p>
              <p className='intake-card__status'>{getStatusInfo(status)}</p>
          </div>
      </article>
    );
}
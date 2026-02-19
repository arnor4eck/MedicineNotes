import './IntakeCard.css'
import { useNavigate } from "react-router-dom";
import {statusService} from "../../../service/statusService.ts";

interface IntakeCardProps {
    id: number;
    name: string;
    status: string;
    date: string;
}

export default function IntakeCard(props: IntakeCardProps){
    const navigate = useNavigate();

    const { id, name, status, date } = props;

    return(
      <article className='intake-card'>
          <div className='intake-card__header'>
              <p className='intake-card__title'>{name}</p>
          </div>
          <div className='intake-card__bottom'>
              <button onClick={() => navigate(`/intakes/${id}`)}
                      className='card__button card__button--primary'> Подробнее</button>
              <p className='intake-card__date'>{date}</p>
              <p className='intake-card__status'>{statusService.getStatusInfo(status)}</p>
          </div>
      </article>
    );
}
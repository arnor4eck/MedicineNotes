import './TemplateCard.css'
import {useNavigate} from "react-router-dom";

interface TemplateCardProps {
    id: number;
    name: string;
    until: string;
    start: string;
}

export default function TemplateCard(props : TemplateCardProps){
    const navigate = useNavigate();

    const { id, name, until, start } = props;

    return(
      <article className='template-card'>
          <div className='template-card__header'>
              <p className='template-card__title'>{name}</p>
          </div>
          <div className='template-card__bottom'>
              <p className='template-card__deadline'><b>C: {start}</b></p>
              <button onClick={() => navigate(`/templates/${id}`)} className='template-card__button template-card__button--primary'> Подробнее</button>
              <p className='template-card__deadline'><b>До: {until}</b></p>
          </div>
      </article>
    );
}
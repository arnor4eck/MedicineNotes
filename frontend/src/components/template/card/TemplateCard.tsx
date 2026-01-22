import './TemplateCard.css'
import {useNavigate} from "react-router-dom";

interface TemplateCardProps {
    id: number;
    name: string;
    until: string;
}

export default function TemplateCard(props : TemplateCardProps){
    const navigate = useNavigate();

    const { id, name, until} = props;

    return(
      <article className='template-card'>
          <div className='template-card__header'>
              <p className='template-card__title'>{name}</p>
          </div>
          <div className='template-card__bottom'>
              <button onClick={() => navigate(`/templates/${id}`)} className='template-card__button template-card__button--primary'> Подробнее</button>
              <p className='template-card__deadline'><b>До: {until}</b></p>
          </div>
      </article>
    );
}
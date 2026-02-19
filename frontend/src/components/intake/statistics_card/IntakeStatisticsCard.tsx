import type {StatusAndCountUnit} from "../../../types/types.ts";
import './IntakeStatisticsCard.css'
import {statusService} from "../../../service/statusService.ts";

interface IntakeStatisticsCardProps {
    name: string;
    units: StatusAndCountUnit[]
}

export default function IntakeStatisticsCard(props: IntakeStatisticsCardProps){

    const { name, units } = props;

    return(
      <article className='intake-statistics-card'>
          <div className="name-part"><h1>{name}</h1></div>
          <div className="statuses-part">
              {units.map((unit, idx) => (
                  <div className="status" key={idx}>
                      {statusService.getStatusInfo(unit.status)}: {unit.count}
                  </div>
              ))}
          </div>
      </article>
    );
}
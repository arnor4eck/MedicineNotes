import './Main.css'
import {useNavigate} from "react-router-dom";

export default function Main(){
    const navigate = useNavigate();

    return(
        <div className='bg'>
            <div className='container'>
                <h1 className='title'>MedicineNotes</h1>
                <p>Следит за вашим здоровьем</p>
                <button onClick={() => navigate('/auth', {replace : true})}>Попробовать</button>
            </div>
        </div>
    )
}
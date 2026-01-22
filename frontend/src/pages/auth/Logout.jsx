import {authService} from "../../service/authService.js";
import {useNavigate} from "react-router-dom";

export default function Logout(){
    authService.logout();
    const navigate = useNavigate();
    setTimeout(() => {
        navigate('/', { replace: true });
    }, 100);

}
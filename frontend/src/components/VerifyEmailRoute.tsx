import { Navigate } from 'react-router-dom';
import { useVerificationStore } from '../storage/verificationStorage.ts';

interface ProtectedRouteProps {
    children: React.ReactNode;
    requireVerification?: boolean;
    redirectTo?: string;
}

export const VerifyEmailRoute: React.FC<ProtectedRouteProps> = ({
                                                              children,
                                                              requireVerification = true,
                                                              redirectTo = '/register'
                                                              }) => {
    const { verificationStarted, email } = useVerificationStore();

    if (requireVerification && (!verificationStarted || !email)) {
        return <Navigate to={redirectTo} replace />;
    }

    return <>{children}</>;
};
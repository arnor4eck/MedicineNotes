import { create } from 'zustand';
import { persist } from 'zustand/middleware';

interface VerificationState {
    email: string | null;
    verificationStarted: boolean;
    startVerification: (email: string) => void;
    completeVerification: () => void;
    resetVerification: () => void;
}

export const useVerificationStore = create<VerificationState>()(
    persist(
        (set) => ({
            email: null,
            verificationStarted: false,

            startVerification: (email) => set({
                email,
                verificationStarted: true
            }),

            completeVerification: () => set({
                verificationStarted: false,
                email: null
            }),

            resetVerification: () => set({
                email: null,
                verificationStarted: false
            }),
        }),
        {
            name: 'verification-storage'
        }
    )
);
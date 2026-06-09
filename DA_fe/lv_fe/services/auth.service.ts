// services/auth.service.ts
import api from '@/lib/axios';

export const authService = {
  login: async (phone: string, password: string) => {
    // Gọi đúng endpoint /auth/login của Backend
    const response = await api.post('/auth/login', { phone, password });
    return response.data; // response.data chính là cái ApiResponse<AuthResponse> của Backend
  },
};
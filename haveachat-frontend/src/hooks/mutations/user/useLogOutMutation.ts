import { API_BASE_URL } from '@haveachat/utils/ApiBaseUrl';
import { useMutation, useQueryClient } from '@tanstack/react-query';

export const useLogOutMutation = () => {
  const queryClient = useQueryClient();


  return useMutation({
    mutationFn: async () => {
      const res = await fetch(`${API_BASE_URL}/auth/oauth/logout`, {
        method: 'POST',
        credentials: 'include',
      });

      if (!res.ok) throw new Error('Logout failed');
    },
    onSuccess: () => {
      queryClient.setQueryData(['me'], null);
    },
  });
};
import { useQuery, UseQueryResult } from '@tanstack/react-query';
import { API_BASE_URL } from '../../../utils/ApiBaseUrl';
import { fetchWithAuth } from '../../utils/fetchWithAuth'

const fetchMe = async () => {
  const res = await fetchWithAuth(`${API_BASE_URL}/api/user`);
  if (!res.ok) throw new Error('Unauthorized');
  return res.json();
};

export const useMe = (): UseQueryResult<User> => {
  return useQuery<User, Error>({
    queryKey: ['me'],
    queryFn: fetchMe,
    retry: false,
    onError: (error) => {
      console.error('useMe error:', error);
      // Optional: redirect to login, toast, etc.
    },
  });
};
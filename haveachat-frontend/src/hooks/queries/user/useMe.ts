import { useQuery, UseQueryOptions } from '@tanstack/react-query';
import { API_BASE_URL } from '@haveachat/utils/ApiBaseUrl';
import { fetchWithAuth } from '@haveachat/hooks/utils/fetchWithAuth';
import { UserDTO } from '@haveachat/api';

const fetchMe = async (): Promise<UserDTO> => {
  const res = await fetchWithAuth(`${API_BASE_URL}/api/user`);
  if (!res.ok) throw new Error('Unauthorized');
  return res.json() as Promise<UserDTO>; // or use await res.json() with a cast
};

export const useMe = (options?: Partial<UseQueryOptions<UserDTO>>) => {
  return useQuery<UserDTO>({
    queryKey: ['me'],
    queryFn: fetchMe,
    retry: false,
    ...options,
  });
};
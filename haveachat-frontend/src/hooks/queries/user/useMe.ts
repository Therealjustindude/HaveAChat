import { useQuery } from '@tanstack/react-query';
import { API_BASE_URL } from '@haveachat/utils/ApiBaseUrl';
import { fetchWithAuth } from '@haveachat/hooks/utils/fetchWithAuth';



const fetchMe = async () => {
  const res = await fetchWithAuth(`${API_BASE_URL}/api/user`);
  if (!res.ok) throw new Error('Unauthorized');
  return res.json();
};

export const useMe = () => {
  return useQuery({
    queryKey: ['me'],
    queryFn: fetchMe,
    retry: false,
  });
};
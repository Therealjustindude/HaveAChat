import { useQuery, UseQueryOptions } from '@tanstack/react-query';
import { API_BASE_URL } from '@haveachat/utils/ApiBaseUrl';
import { fetchWithAuth } from '@haveachat/hooks/utils/fetchWithAuth';
import { Channel } from '@haveachat/api';

const fetchChannels = async (): Promise<Array<Channel>> => {
	const res = await fetchWithAuth(`${API_BASE_URL}/api/channels`);
	if (!res.ok) throw new Error('Unauthorized');
	return res.json() as Promise<Array<Channel>>;
};

export const useGetChannels = (options?: Partial<UseQueryOptions<Array<Channel>>>) => {
	return useQuery<Array<Channel>>({
		queryKey: ['channels'],
		queryFn: fetchChannels,
		retry: false,
		staleTime: 1000 * 60,
		...options,
	});
};
import { useQuery, UseQueryOptions } from '@tanstack/react-query';
import { API_BASE_URL } from '@haveachat/utils/ApiBaseUrl';
import { fetchWithAuth } from '@haveachat/hooks/utils/fetchWithAuth';
import { Channel, ChatMessageDTO, UserDTO } from '@haveachat/api';

const fetchChatHistory = async (channelId: string): Promise<Array<ChatMessageDTO>> => {
	const res = await fetchWithAuth(`${API_BASE_URL}/api/chatMessage/history/${channelId}`);
	if (!res.ok) throw new Error('Unauthorized');
	return res.json() as Promise<Array<ChatMessageDTO>>;
};

export const useGetChatHistory = (
	channelId: string,
	options?: Partial<UseQueryOptions<Array<ChatMessageDTO>>>
) => {
	return useQuery<Array<ChatMessageDTO>>({
		queryKey: ['chatHistory', channelId],
		queryFn: () => fetchChatHistory(channelId),
		enabled: !!channelId,
		retry: false,
		staleTime: 1000 * 60,
		...options,
	});
};
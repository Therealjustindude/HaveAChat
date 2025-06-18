import { useMutation, UseMutationOptions } from '@tanstack/react-query';
import { API_BASE_URL } from '@haveachat/utils/ApiBaseUrl';
import { fetchWithAuth } from '@haveachat/hooks/utils/fetchWithAuth';
import { Channel, CreateChannelRequest } from '@haveachat/api';

const createChannel = async (payload: CreateChannelRequest): Promise<Channel> => {
  const res = await fetchWithAuth(`${API_BASE_URL}/api/channels`, {
    method: 'POST',
    headers: {
      'Content-Type': 'application/json',
    },
    body: JSON.stringify(payload),
  });
  if (!res.ok) throw new Error('Failed to create channel');
  return res.json() as Promise<Channel>;
};

export const useCreateChannel = (
  options?: UseMutationOptions<Channel, Error, CreateChannelRequest>
) => {
  return useMutation<Channel, Error, CreateChannelRequest>({
    mutationFn: createChannel,
    ...options,
  });
};
import { Channel } from '@haveachat/components/Channel';
import { useGetChatHistory } from '@haveachat/hooks/queries/chat-message/useGetChatHistory';
import { createFileRoute, useParams } from '@tanstack/react-router'

export const Route = createFileRoute('/chat/$channelId')({
  component: ChannelRoute,
})

function ChannelRoute() {
	const { channelId } = useParams({ from: '/chat/$channelId' });
	const { data: chatHistory, isFetching } = useGetChatHistory(channelId);
  return <Channel chatHistory={chatHistory} isFetching={isFetching} />;
}

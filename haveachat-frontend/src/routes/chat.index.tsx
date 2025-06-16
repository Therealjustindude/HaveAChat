import { ChannelList } from '@haveachat/components/ChannelList';
import { createFileRoute } from '@tanstack/react-router'

export const Route = createFileRoute('/chat/')({
  component: ChannelList,
});

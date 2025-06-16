import { ChannelList } from '@haveachat/components/ChannelList';
import { useGetChannels } from '@haveachat/hooks/queries/channel/useGetChannels';
import { IconMessages } from '@tabler/icons-react';
import { createFileRoute, Link, Outlet, useRouter, useMatches } from '@tanstack/react-router'

export const Route = createFileRoute('/chat')({
  component: ChatLayout,
});

function ChatLayout() {
  const matches = useMatches();
  const { data: channels, isFetching } = useGetChannels();
  const channelMatch = matches.find(m => m.routeId === '/chat/$channelId');
  const channelId = channelMatch?.params?.channelId;
  const isChannel = Boolean(channelId);
  
  const activeChannel = channels?.find(
    (channel) => String(channel.id) === String(channelId)
  );

  return (
    <div className="min-h-screen flex flex-col">
      {/* Top Bar */}
      <header
        className={`sticky top-12 h-8 px-4 z-40 bg-background flex items-center justify-between border-b
          ${!isChannel ? 'hidden md:flex' : 'flex'}
          md:top-0 md:h-12 md:justify-center md:flex`}
      >
        <div className='flex items-center gap-2'>
          <IconMessages className='text-green-600 w-4 h-4' />
          <span className="font-semibold">
            {activeChannel ? activeChannel.name : '...'}
          </span>
        </div>
        {isChannel && (
          <Link
            to="/chat"
            className="md:hidden rounded bg-background block"
          >
            Back
          </Link>
        )}
      </header>

      {/* Main Content */}
      <div className="flex flex-1 relative md:border-l">
        {/* Sidebar: show on desktop, hide on mobile */}
        <aside className="w-64 bg-background hidden md:block md:sticky md:top-12 h-[calc(100vh-3rem)] border-r">
          {
            isFetching ? <p>Loading...</p>
              : <ChannelList channels={channels} isFetching={isFetching} />
          }
        </aside>

        {/* Main panel: always rendered */}
        <main className="flex-1 h-full overflow-y-auto">
          {/* On desktop: if not in a channel, render nothing (since sidebar shows ChannelList) */}
          {/* On mobile: if not in a channel, render ChannelList; if in a channel, render Outlet */}
          {isChannel
            ? <Outlet />
            : <div className="block md:hidden"><ChannelList channels={channels} isFetching={isFetching} /></div>
          }
        </main>
      </div>
    </div>
  );
}

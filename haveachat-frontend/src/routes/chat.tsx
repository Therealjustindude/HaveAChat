import { useAuth } from '@haveachat/auth/AuthProvider';
import { ChannelList } from '@haveachat/components/ChannelList';
import { useGetChannels } from '@haveachat/hooks/queries/channel/useGetChannels';
import { IconUserFilled } from '@tabler/icons-react';
import { createFileRoute, Link, Outlet, useRouter, useMatches } from '@tanstack/react-router'

export const Route = createFileRoute('/chat')({
  component: ChatLayout,
});

function ChatLayout() {
  const { user } = useAuth();
  const matches = useMatches();
  const { data: channels, isFetching } = useGetChannels();
  const channelMatch = matches.find(m => m.routeId === '/chat/$channelId');
  const channelId = channelMatch?.params?.channelId;
  const isChannel = Boolean(channelId);
  
  const activeChannel = channels?.find(
    (channel) => String(channel.id) === String(channelId)
  );

  return (
    <div className="min-h-screen flex flex-col overflow-hidden">
      {/* Top Bar */}
      <header className="flex items-center gap-2 p-4 border-b">
        <IconUserFilled className="text-green-400 w-4 h-4" />
        <span className="font-bold text-sm">{user?.name}</span>
        {isChannel && (
          <>
            <span className="mx-2 text-gray-400">/</span>
            <span className="font-semibold">
              {activeChannel ? activeChannel.name : `Channel ${channelId}`}
            </span>
          </>
        )}
        {/* Back button: only show on mobile (block on mobile, hidden on md+) */}
        {isChannel && (
          <Link
            to="/chat"
            className="ml-auto px-3 py-1 rounded bg-background block md:hidden"
          >
            Back
          </Link>
        )}
      </header>

      {/* Main Content */}
      <div className="flex flex-1 overflow-hidden">
        {/* Sidebar: show on desktop, hide on mobile */}
        <aside className={`w-32 border-r bg-background hidden md:block`}>
          {
            isFetching ? <p>Loading...</p>
              : <ChannelList channels={channels} isFetching={isFetching} />
          }
        </aside>

        {/* Main panel: always rendered */}
        <main className="flex-1 h-full overflow-y-auto p-4">
          {/* On desktop: if not in a channel, render nothing (since sidebar shows ChannelList) */}
          {/* On mobile: if not in a channel, render ChannelList; if in a channel, render Outlet */}
          {isChannel ? <Outlet /> : <div className="block md:hidden"><ChannelList channels={channels} isFetching={isFetching} /></div>}
        </main>
      </div>
    </div>
  );
}

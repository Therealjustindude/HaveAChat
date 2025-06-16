import { Channel } from "@haveachat/api";
import { Link } from "@tanstack/react-router";

export const ChannelList = ({
	channels,
	isFetching
}: ChannelListProps) => (
	<div className="p-4">
    {isFetching ? (
      <p>Loading...</p>
		) : (channels?.length ?? 0) > 0 ? (
				<div className="flex flex-col items-start gap-2 w-full overflow-hidden">
					{(channels ?? []).map((channel) => (
						<Link
							key={channel.id}
							to="/chat/$channelId"
							params={{ channelId: String(channel.id) }}
							className="[&.active]:font-bold truncate w-full"
						>
							{channel.name}
						</Link>
					))}
				</div>
    ) : (
      <p>No channels for user</p>
    )}
  </div>
);

export type ChannelListProps = {
	channels: Channel[] | undefined;
	isFetching?: boolean;
}
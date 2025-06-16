import { Channel } from "@haveachat/api";
import { IconGolfFilled, IconPlus } from "@tabler/icons-react";
import { Link } from "@tanstack/react-router";
import {
  Accordion,
  AccordionItem,
  AccordionTrigger,
  AccordionContent,
} from "@haveachat/components/ui/accordion";
import { Button } from "@haveachat/components/ui/button";

export const ChannelList = ({
  channels,
  isFetching,
}: ChannelListProps) => {
  const publicChannels = (channels ?? []).filter((c) => !c.privateChannel);
  const directMessages = (channels ?? []).filter((c) => c.privateChannel);

  return (
    <div className="py-2 px-4">
      {isFetching ? (
        <p>Loading...</p>
      ) : (channels?.length ?? 0) > 0 ? (
        <Accordion
          type="multiple"
          defaultValue={["channels", "directMessages"]}
          className="w-full"
        >
          {/* Channels Accordion */}
          <AccordionItem value="channels">
						<div className="flex items-center justify-between w-full">
							<AccordionTrigger>Channels</AccordionTrigger>
							<Button
								type="button"
								tabIndex={0}
								onClick={(e) => {
									e.stopPropagation(); // Prevents accordion toggle
									console.log("add channel button clicked")
								}}
								className="h-0.5 w-0.5 rounded hover:bg-accent cursor-pointer"
								aria-label="start a new channel"
							>
								<IconPlus />
							</Button>
						</div>
            <AccordionContent>
              {publicChannels.length > 0 ? (
                <div className="flex flex-col gap-2">
                  {publicChannels.map((channel) => (
                    <Link
                      key={channel.id}
                      to="/chat/$channelId"
                      params={{ channelId: String(channel.id) }}
                      className="flex items-center gap-2 w-full truncate [&.active]:text-green-600 [&.active]:font-semibold"
                      title={channel.name}
                    >
                      <IconGolfFilled className="w-4 h-4" />
                      <span className="truncate flex-1">{channel.name}</span>
                    </Link>
                  ))}
                </div>
              ) : (
                <p className="text-muted-foreground text-sm">No channels</p>
              )}
            </AccordionContent>
          </AccordionItem>

          {/* Direct Messages Accordion */}
						<AccordionItem value="directMessages">
							<div className="flex items-center justify-between w-full">
								<AccordionTrigger>Direct Messages</AccordionTrigger>
								<Button
									type="button"
									tabIndex={0}
									onClick={(e) => {
										e.stopPropagation(); // Prevents accordion toggle
										console.log("add DM button clicked")
									}}
									className="h-0.5 w-0.5 rounded hover:bg-accent cursor-pointer"
									aria-label="Start a direct message"
								>
									<IconPlus />
								</Button>
							</div>
            <AccordionContent>
              {directMessages.length > 0 ? (
                <div className="flex flex-col gap-2">
                  {directMessages.map((channel) => (
                    <Link
                      key={channel.id}
                      to="/chat/$channelId"
                      params={{ channelId: String(channel.id) }}
                      className="flex items-center gap-2 w-full truncate [&.active]:text-green-600 [&.active]:font-semibold"
                      title={channel.name}
                    >
                      <IconGolfFilled className="w-4 h-4" />
                      <span className="truncate flex-1">{channel.name}</span>
                    </Link>
                  ))}
                </div>
              ) : (
                <p className="text-muted-foreground text-sm">No direct messages</p>
              )}
            </AccordionContent>
          </AccordionItem>
        </Accordion>
      ) : (
        <p>No channels for user</p>
      )}
    </div>
  );
};

export type ChannelListProps = {
  channels: Channel[] | undefined;
  isFetching?: boolean;
};
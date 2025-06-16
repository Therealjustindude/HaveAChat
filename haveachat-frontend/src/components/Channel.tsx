import { ChatMessageDTO } from "@haveachat/api";
import { ChatMessage } from "./ui/ChatMessage";


export const Channel = ({
	chatHistory,
	isFetching
} : ChannelProps) => {
	return (
		<div className="min-h-screen flex-col">
			{isFetching ? (
				<p>Loading...</p>
			) : (chatHistory?.length ?? 0) > 0 ? (
				<div className="flex flex-col items-start gap-2 w-full overflow-hidden">
					{(chatHistory ?? []).map((chat) => (
						<ChatMessage chatMessage={chat} />
					))}
				</div>
			) : (
				<p>No chat history</p>
			)}
		</div>
	);
}

export type ChannelProps = {
	chatHistory: ChatMessageDTO[] | undefined;
	isFetching?: boolean;
}
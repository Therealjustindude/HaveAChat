import { ChatMessageDTO } from "@haveachat/api"
import { formatDate } from "@haveachat/utils/formatDate";
import { IconUserCircle } from "@tabler/icons-react";

export const ChatMessage = ({
	chatMessage
} : ChatMessageProps) => {
	return (
		<div className="flex gap-2 items-center">
			<IconUserCircle />
			<div className="flex flex-col gap-2">
				<div className="flex gap-2">
					<p>{chatMessage?.userName}</p>
					<p>{formatDate(chatMessage?.createdAt)}</p>
				</div>
				<div>
					{chatMessage?.message}
				</div>
			</div>
		</div>
	)
}

export type ChatMessageProps = {
	chatMessage: ChatMessageDTO | undefined
}
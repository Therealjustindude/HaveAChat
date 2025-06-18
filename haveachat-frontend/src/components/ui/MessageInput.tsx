import { Textarea } from "@haveachat/components/ui/textarea";
import { Button } from "@haveachat/components/ui/button";
import { useState } from "react";

export function MessageInput({
	onSend,
	placeholder = "Type a message...",
	isDisabled
}: MessageInputProps) {
  const [value, setValue] = useState("");
  return (
    <form
      onSubmit={e => {
        e.preventDefault();
        if (value.trim()) {
          onSend(value);
          setValue("");
        }
      }}
      className="flex gap-2 items-center w-full max-w-full"
    >
      <Textarea
        value={value}
        onChange={e => setValue(e.target.value)}
        placeholder={placeholder}
				className="flex-1 w-full max-w-full"
      />
			<Button
				type="submit"
				disabled={isDisabled}
			>
				{isDisabled ? "Creating..." : "Send"}
			</Button>
    </form>
  );
}

type MessageInputProps = {
	onSend: (value: string) => void;
	placeholder?: string,
	isDisabled: boolean;
}


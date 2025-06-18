import {
  Dialog,
  DialogContent,
  DialogHeader,
  DialogTitle,
  DialogFooter,
  DialogClose,
} from "@haveachat/components/ui/dialog";
import { Label } from "@haveachat/components/ui/label";
import { Button } from "@haveachat/components/ui/button";
import { useCreateChannel } from "@haveachat/hooks/mutations/channel/useCreateChannel";
import { MessageInput } from "@haveachat/components/ui/MessageInput";
import { UserMultiSelect } from "@haveachat/components/ui/UserMultiSelect";
import { UserDTO } from "@haveachat/api";
import { useState } from "react";

export function CreateDMDialog({
  open,
  onOpenChange,
  onCreated,
}: {
  open: boolean;
  onOpenChange: (open: boolean) => void;
  onCreated?: () => void;
	}) {
	const [selectedUsers, setSelectedUsers] = useState<(string | undefined)[]>([]);

	// TODO: Add endpoint to fetch friends to DM 
	const users: UserDTO[] = []; 

	
  const createChannelMutation = useCreateChannel({
    onSuccess: () => {
      onCreated?.();
      onOpenChange(false);
    },
  });

  const handleCreate = () => {
    createChannelMutation.mutate({
			privateChannel: true,
			type: "DM"
    });
  };

  return (
    <Dialog open={open} onOpenChange={onOpenChange}>
      <DialogContent>
        <DialogHeader>
          <DialogTitle>Start Direct Message</DialogTitle>
        </DialogHeader>
        <div className="flex flex-col gap-4">
          <div className="flex flex-col gap-2">
            <Label htmlFor="dm-name">Add Users</Label>
						<UserMultiSelect
							users={users}
							selected={selectedUsers}
							onChange={setSelectedUsers}
						/>
          </div>
          <div className="w-full max-w-md">
						<MessageInput
							onSend={msg => console.log(msg)}
							isDisabled={createChannelMutation.isPending}
						/>
          </div>
        </div>
        <DialogFooter>
          <DialogClose asChild>
            <Button variant="outline" type="button">
              Cancel
            </Button>
          </DialogClose>
        </DialogFooter>
      </DialogContent>
    </Dialog>
  );
}
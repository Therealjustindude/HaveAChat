import { useState } from "react";
import {
  Dialog,
  DialogContent,
  DialogHeader,
  DialogTitle,
  DialogFooter,
  DialogClose,
} from "@haveachat/components/ui/dialog";
import { Input } from "@haveachat/components/ui/input";
import { Checkbox } from "@haveachat/components/ui/checkbox";
import { Label } from "@haveachat/components/ui/label";
import { Button } from "@haveachat/components/ui/button";
import { useCreateChannel } from "@haveachat/hooks/mutations/channel/useCreateChannel";

export function CreateChannelDialog({
  open,
  onOpenChange,
  onCreated,
}: {
  open: boolean;
  onOpenChange: (open: boolean) => void;
  onCreated?: () => void;
}) {
  const [name, setName] = useState("");
  const [description, setDescription] = useState("");
  const [privateChannel, setPrivateChannel] = useState(false);

  const createChannelMutation = useCreateChannel({
    onSuccess: () => {
      setName("");
      setDescription("");
      setPrivateChannel(false);
      onOpenChange(false);
      onCreated?.();
    },
  });

  const handleCreate = () => {
    createChannelMutation.mutate({
      name,
      description,
			privateChannel,
			type: privateChannel ? "PRIVATE" : "PUBLIC"
    });
  };

  return (
    <Dialog open={open} onOpenChange={onOpenChange}>
      <DialogContent>
        <DialogHeader>
          <DialogTitle>Create Channel</DialogTitle>
        </DialogHeader>
        <div className="flex flex-col gap-4">
          <div className="flex flex-col gap-2">
            <Label htmlFor="channel-name">Name</Label>
            <Input
              id="channel-name"
              value={name}
              onChange={e => setName(e.target.value)}
              placeholder="Channel name"
            />
          </div>
          <div className="flex flex-col gap-2">
            <Label htmlFor="channel-description">Description</Label>
            <Input
              id="channel-description"
              value={description}
              onChange={e => setDescription(e.target.value)}
              placeholder="Description"
            />
          </div>
          <div className="flex items-center gap-2">
            <Checkbox
              id="private-channel"
              checked={privateChannel}
              onCheckedChange={checked => setPrivateChannel(!!checked)}
            />
            <Label htmlFor="private-channel">Private Channel</Label>
          </div>
        </div>
        <DialogFooter>
          <Button onClick={handleCreate} disabled={createChannelMutation.isPending}>
            {createChannelMutation.isPending ? "Creating..." : "Create"}
          </Button>
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
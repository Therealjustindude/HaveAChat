import { useState, useRef, useEffect } from "react";
import { Checkbox } from "@haveachat/components/ui/checkbox";
import { Input } from "@haveachat/components/ui/input";
import { UserDTO } from "@haveachat/api";

export function UserMultiSelect({
  users,
  selected,
  onChange,
}: UserMultiSelectProps) {
  const [query, setQuery] = useState("");
  const [showDropdown, setShowDropdown] = useState(false);
  const wrapperRef = useRef<HTMLDivElement>(null);

  const filteredUsers = (users ?? []).filter(
    user =>
      user.name?.toLowerCase().includes(query.toLowerCase()) ||
      user.email?.toLowerCase().includes(query.toLowerCase())
  );

  useEffect(() => {
		function handleFocusOut(e: FocusEvent) {
			setTimeout(() => {
				if (
					wrapperRef.current &&
					!wrapperRef.current.contains(document.activeElement)
				) {
					setShowDropdown(false);
				}
			}, 0);
		}
		const node = wrapperRef.current;
		if (node) node.addEventListener("focusout", handleFocusOut);
		return () => {
			if (node) node.removeEventListener("focusout", handleFocusOut);
		};
	}, []);

  return (
    <div className="relative w-full" ref={wrapperRef}>
      <Input
        placeholder="Search users..."
        value={query}
        onChange={e => {
          setQuery(e.target.value);
          if (!showDropdown) setShowDropdown(true);
        }}
        onInput={() => setShowDropdown(true)}
        className="mb-2"
      />
      {showDropdown && (
        <div
          className="absolute left-0 z-20 mt-1 w-full max-h-48 rounded border bg-popover shadow-lg overflow-y-auto flex flex-col gap-1"
        >
          {filteredUsers.length === 0 && (
            <div className="text-muted-foreground text-sm px-2 py-1">No users found</div>
          )}
          {filteredUsers.map((user, idx) => (
            <label
              key={user.email}
              className="flex items-center gap-2 px-2 py-1 rounded hover:bg-accent cursor-pointer"
              tabIndex={0}
            >
              <Checkbox
                checked={(selected ?? []).includes(user.email)}
                onCheckedChange={() => {
                  if ((selected ?? []).includes(user.email)) {
                    onChange(selected.filter(id => id !== user.email));
                  } else {
                    onChange([...(selected ?? []), user.email]);
                  }
                }}
                tabIndex={0}
              />
              <span>{user.name}</span>
              <span className="text-xs text-muted-foreground">{user.email}</span>
            </label>
          ))}
        </div>
      )}
    </div>
  );
}

type UserMultiSelectProps = {
  users: UserDTO[];
  selected: (string | undefined)[];
  onChange: (selected: (string | undefined)[]) => void;
};
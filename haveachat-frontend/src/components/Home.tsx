import { useAuth } from "@haveachat/auth/AuthProvider";

export const Home = () => {
	const { user } = useAuth();
	return (
		<div className="min-h-screen flex-col">
			<div className="flex flex-col gap-1">
				<h1 className="text-2xl">Welcome, {user?.name}</h1>
				<p>Looks like a good day to play.</p>
			</div>
		</div>
	);
}
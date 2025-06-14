import { ReactNode } from "react";
import { ThemeToggle } from "@haveachat/components/theme/ThemeToggle";
import { useAuth } from "@haveachat/auth/AuthProvider";
import { IconLogout } from "@tabler/icons-react";
import { Button } from "@haveachat/components/ui/button";
import { Link } from "@tanstack/react-router";


export const AppLayout = ({ children }: { children: ReactNode }) => {
	const { logout, user  } = useAuth();
	
	const handleLogout = async () => {
		try {
			await logout();
			window.location.href = '/login';
		} catch (err) {
			console.error('Logout failed', err);
		}
	};

	return (
		<div className="min-h-screen flex flex-col md:flex-row bg-background text-foreground">
			{/* Sidebar (header on desktop) */}
			<nav className="p-4 border-b bg-background md:border-b-0 md:border-r flex justify-between md:flex-col md:justify-between md:w-auto sticky top-0  md:h-screen z-50">
				{/* Desktop-only nav links */}
				<div className="flex md:flex-col gap-2 items-center">
					<div className="flex gap-2 items-center">
						⛳️ <p className="font-pacifico text-lg font-semibold text-green-600">Have A Chat</p>
					</div>
					{
						user && (
							<div className="hidden md:flex flex-col gap-2 items-start">
								<Link to="/" className="[&.active]:font-bold">
									Home
								</Link>
								<Link to="/chat" className="[&.active]:font-bold">
									Chat
								</Link>
							</div>
						)
					}
				</div>

				<div className="flex items-center gap-2 justify-end">
					{user && (
						<Button
							onClick={handleLogout}
							variant="outline"
							size="sm"
						>
							<IconLogout /> Logout
						</Button>
					)}
					<ThemeToggle />
				</div>
			</nav>

			{/* Main content area */}
			<main className="flex-1 p-4">
				{children}
			</main>

			{/* Mobile Footer Nav */}
			{
				user && (
					<footer className="fixed bottom-0 left-0 right-0 bg-background border-t md:hidden flex justify-around items-center h-16 z-50">
						<Link to="/" className="[&.active]:font-bold text-sm">
							Home
						</Link>
						<Link to="/chat" className="[&.active]:font-bold text-sm">
							Chat
						</Link>
					</footer>
				)
			}
		</div>
  );
}
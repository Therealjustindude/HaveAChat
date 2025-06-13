import { useAuth } from "@haveachat/auth/AuthProvider";
import { Button } from "@haveachat/components/ui/button";


export const Home = () => {
	const { logout } = useAuth();

  const handleLogout = async () => {
    try {
      await logout();
      window.location.href = '/login';
    } catch (err) {
      console.error('Logout failed', err);
    }
  };

	return (
		<>
			<h1>Home</h1>
			<Button
				onClick={handleLogout}
			>
				Log out
			</Button>
		</>
	);
}
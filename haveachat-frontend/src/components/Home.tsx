import { useAuth } from "@haveachat/auth/AuthProvider";

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
			<button
				type="button"
				onClick={handleLogout}
			>
				Log out
			</button>
		</>
	);
}
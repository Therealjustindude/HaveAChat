import { useMutation } from '@tanstack/react-query';

export const Home = () => {
	const logoutMutation = useMutation({
		mutationFn: async () => {
			await fetch('http://localhost:8080/logout', {
				method: 'POST',
				credentials: 'include',  // Make sure cookies are sent
			});
		},
		onSuccess: () => {
			// Optionally redirect after logout, clear local state, etc.
			localStorage.clear(); // Example of clearing local storage if you use it
			window.location.href = 'http://localhost:3000/login';
		},
		onError: (error) => {
			console.error('Logout failed', error);
		}
	});

	const handleLogout = () => {
		logoutMutation.mutate();
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
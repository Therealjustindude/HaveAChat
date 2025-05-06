import { useMutation } from '@tanstack/react-query';
import { checkAuth } from '../utils/checkAuth';

export const Home = () => {
	const logoutMutation = useMutation({
		mutationFn: async () => {
			try {
				const { csrfToken } = await checkAuth();
				console.log('csrftoken', csrfToken);  // Check the CSRF token
				if (csrfToken) {
					const headers: HeadersInit = {
						'X-XSRF-TOKEN': csrfToken,
					};
					await fetch('http://localhost:8080/api/logout', {
						method: 'POST',
						credentials: 'include',
						headers: headers,
					});
				} else {
					console.error('No CSRF token found.');
				}
			} catch (error) {
				console.error('Error during logout mutation:', error);
			}
		},
		onSuccess: () => {
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
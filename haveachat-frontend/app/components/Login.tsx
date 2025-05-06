export const Login = () => {
	const handleGoogleLogin = () => {
    window.location.href = 'http://localhost:8080/auth/google/login';
  };
  
	return (
		<button
      type="button"
      onClick={handleGoogleLogin}
    >
      Google Sign In
    </button>
	);
}
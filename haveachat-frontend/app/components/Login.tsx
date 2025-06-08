import { useAuth } from "@haveachat/auth/AuthProvider";
import { API_BASE_URL } from "@haveachat/utils/ApiBaseUrl";
import { useGoogleLogin } from "@react-oauth/google";
import { useRouter } from '@tanstack/react-router';


export const Login = () => {
  const { navigate } = useRouter();
  const { login: refreshUser } = useAuth();

  const login = useGoogleLogin({
    onSuccess: async tokenResponse => {
      const response = await fetch(`${API_BASE_URL}/auth/oauth/google`, {
        method: 'POST',
        headers: {
          'Content-Type': 'application/json',
        },
        credentials: 'include',
        body: JSON.stringify({
          accessToken: tokenResponse.access_token,
        }),
      });

      if (!response.ok) throw new Error('Failed to login');

      await refreshUser();

      navigate({ to: '/' });
    },
    onError: () => console.log('Login Failed'),
  });
  
  return (
		<button
      type="button"
      onClick={() => login()}
    >
      Google Sign In
    </button>
	);
}
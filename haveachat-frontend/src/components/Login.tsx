import { useAuth } from "@haveachat/auth/AuthProvider";
import { API_BASE_URL } from "@haveachat/utils/ApiBaseUrl";
import { useGoogleLogin } from "@react-oauth/google";
import { useRouter } from '@tanstack/react-router';
import { Button } from "@haveachat/components/ui/button";
import { IconBrandGmail } from '@tabler/icons-react';


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
		<Button
      onClick={() => login()}
      variant="outline"
      size="sm"
    >
      <IconBrandGmail /> Google Sign In
    </Button>
	);
}
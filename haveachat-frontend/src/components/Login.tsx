import { useAuth } from "@haveachat/auth/AuthProvider";
import { API_BASE_URL } from "@haveachat/utils/ApiBaseUrl";
import { useGoogleLogin } from "@react-oauth/google";
import { useRouter } from '@tanstack/react-router';
import { Button } from "@haveachat/components/ui/button";
import { IconBrandGmail } from '@tabler/icons-react';
import {
  Card,
  CardDescription,
  CardFooter,
  CardHeader,
  CardTitle,
} from "@haveachat/components/ui/card"


export const Login = () => {
  const { navigate } = useRouter();

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

      navigate({ to: '/' });
    },
    onError: () => console.log('Login Failed'),
  });
  
  return (
    <div className="min-h-screen flex items-center justify-center">
      <div className="flex flex-col items-center gap-8">
        <div className="flex items-center gap-2">
          <div className="flex flex-col gap-2">
            <p className="font-pacifico text-5xl font-semibold text-green-600">Have A Chat</p>
            <p className="text-xs text-muted-foreground">A social app for golfers who'd rather be playing.</p>
          </div>
        </div>
        <Card className="w-full max-w-sm justify-around">
          <CardHeader>
            <CardTitle>Login to your account</CardTitle>
            <CardDescription>Use Google to sign in or create your account. No password needed.</CardDescription>
          </CardHeader>
          <CardFooter className="justify-center">
            <Button
              onClick={() => login()}
              variant="outline"
              size="sm"
            >
              <IconBrandGmail /> Login with Google
            </Button>
          </CardFooter>
        </Card>
      </div>
    </div>
	);
}
import { createFileRoute, useRouter } from '@tanstack/react-router'
import { Home } from '../components/Home'
import { useAuth } from '../auth/AuthProvider';
import { useEffect } from 'react';

export const Route = createFileRoute('/')({
  component: HomeComp,
})

function HomeComp() {
  const { user, userIsLoading } = useAuth();
  const { navigate } = useRouter();

  useEffect(() => {
    if (!user && !userIsLoading) {
      navigate({ to: '/login', replace: true });
    }
  }, [user, userIsLoading, navigate]);

  if (userIsLoading || !user) return <div>Loading...</div>;

  return <Home />;
}
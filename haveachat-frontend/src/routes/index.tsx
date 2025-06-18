import { createFileRoute, redirect, useRouter } from '@tanstack/react-router'
import { Home } from '@haveachat/components/Home'
import { useAuth } from '@haveachat/auth/AuthProvider';
import { useEffect } from 'react';

export const Route = createFileRoute('/')({
  beforeLoad: ({ context }) => {
    if (!context.user?.isAuthed) {
      throw redirect({
        to: '/login',
      })
    }
  },
  component: HomeComp,
})

function HomeComp() {
  const { user, userIsLoading } = useAuth();

  if (userIsLoading || !user) return <div>Loading...</div>;

  return <Home />;
}
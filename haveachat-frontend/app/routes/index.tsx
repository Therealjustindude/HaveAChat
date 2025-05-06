import { createFileRoute, redirect } from '@tanstack/react-router'
import { Home } from '../components/Home'

export const Route = createFileRoute('/')({
  component: HomeComp,
  beforeLoad: async ({ context }) => {
    if (!context.authState.isAuthenticated) {
      throw redirect({ to: '/login' })
    }
  },
})

function HomeComp() {
  return (
    <Home />
  )
}
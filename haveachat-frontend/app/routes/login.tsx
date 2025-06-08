import { createFileRoute } from '@tanstack/react-router'
import { Login } from '@haveachat/components/Login'

export const Route = createFileRoute('/login')({
  component: LoginComp,
})

function LoginComp() {
  return <Login />
}
import { Login } from '@haveachat/components/Login'
import { createFileRoute } from '@tanstack/react-router'

export const Route = createFileRoute('/_authed')({
	beforeLoad: ({ context }) => {
		console.log('/_authed', context)
    if (!context.user) {
      throw new Error('Not authenticated')
    }
  },
  errorComponent: ({ error }) => {
    if (error.message === 'Not authenticated') {
      return (
        <div className="flex items-center justify-center p-12">
          <Login />
        </div>
      )
    }

    throw error
  },
})
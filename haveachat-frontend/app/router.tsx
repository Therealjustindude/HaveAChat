import { createRouter as createTanStackRouter } from '@tanstack/react-router';
import { routeTree } from './routeTree.gen';

export interface AuthState {
  isAuthenticated: boolean;
  user: any | null;
}

export interface RouterContext {
  authState: AuthState;
}

export function createRouter(context: RouterContext) {
  const router = createTanStackRouter({
    routeTree,
    scrollRestoration: true,
    context,
  });

  return router;
}

declare module '@tanstack/react-router' {
  interface Register {
    router: ReturnType<typeof createRouter>
  }
}
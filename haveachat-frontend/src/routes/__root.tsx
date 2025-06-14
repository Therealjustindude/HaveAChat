import type { ReactNode } from 'react'
import {
  Outlet,
  HeadContent,
  Scripts,
  createRootRoute,
} from '@tanstack/react-router';
import { QueryClient, QueryClientProvider } from '@tanstack/react-query';
import { GoogleOAuthProvider } from '@react-oauth/google';
import { AuthProvider } from '@haveachat/auth/AuthProvider';
import appCss from "@haveachat/styles/app.css?url"
import { ThemeProvider } from '@haveachat/components/theme/ThemeProvider';
import { AppLayout } from '@haveachat/components/AppLayout';

const queryClient = new QueryClient();

const clientId = import.meta.env.VITE_GOOGLE_OAUTH_CLIENT_ID;

export const Route = createRootRoute({
  head: () => ({
    meta: [
      { charSet: 'utf-8' },
      { name: 'viewport', content: 'width=device-width, initial-scale=1' },
      { title: 'HaveAChat' },
    ],
    links: [
      {
        rel: "stylesheet",
        href: appCss,
      },
    ],
  }),
  component: RootComponent,
});

function RootComponent() {
  return (
    <RootDocument>
      <GoogleOAuthProvider clientId={clientId}>
        <QueryClientProvider client={queryClient}>
          <AuthProvider>
            <ThemeProvider defaultTheme="dark" storageKey="vite-ui-theme">
              <AppLayout>
                <Outlet />
              </AppLayout>
            </ThemeProvider>
          </AuthProvider>
        </QueryClientProvider>
      </GoogleOAuthProvider>
    </RootDocument>
  );
}

function RootDocument({ children }: Readonly<{ children: ReactNode }>) {
  return (
    <html>
      <head>
        <HeadContent />
      </head>
      <body>
        {children}
        <Scripts />
      </body>
    </html>
  )
}
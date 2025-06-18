import { type ReactNode } from 'react'
import {
  Outlet,
  HeadContent,
  Scripts,
  createRootRoute,
  useRouteContext,
  redirect
} from '@tanstack/react-router';
import { QueryClient, QueryClientProvider } from '@tanstack/react-query';
import { GoogleOAuthProvider } from '@react-oauth/google';
import { AuthProvider } from '@haveachat/auth/AuthProvider';
import appCss from "@haveachat/styles/app.css?url"
import { ThemeProvider } from '@haveachat/components/theme/ThemeProvider';
import { AppLayout } from '@haveachat/components/AppLayout';
import { createServerFn } from '@tanstack/react-start';
import { DefaultCatchBoundary } from '@haveachat/components/ui/DefaultCatchBoundary';
import { NotFound } from '@haveachat/components/ui/NotFound';
import { getWebRequest } from '@tanstack/react-start/server'
import { getUserFromRequest } from '@haveachat/utils/getUserFromRequest';
import { UserDTO } from '@haveachat/api';

const clientId = import.meta.env.VITE_GOOGLE_OAUTH_CLIENT_ID;
const queryClient = new QueryClient();

export type MyRouteContextReturn = {
    isAuthed: boolean,
    email: string | undefined
};

const fetchAuth = createServerFn({ method: 'GET' }).handler(async (): Promise<MyRouteContextReturn | null> => {
  const request = getWebRequest();
  if (!request) return null;

  const cookieUser = await getUserFromRequest(request);
  if (!cookieUser) {
    return null;
  }

  return {
    isAuthed: true,
    email: cookieUser.email,
  };
});

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
  beforeLoad: async (): Promise<{ user: MyRouteContextReturn | null }> => {
    const user = await fetchAuth()
    console.log('[beforeload - root - user]:', user)

    return {
      user
    }
  },
  errorComponent: (props) => {
    return (
      <RootDocument>
        <DefaultCatchBoundary {...props} />
      </RootDocument>
    )
  },
  notFoundComponent: () => <NotFound />,
  component: RootComponent,
})


function RootComponent() {
  const context = useRouteContext({ from: '__root__' });

  return (
    <RootDocument>
      <GoogleOAuthProvider clientId={clientId}>
        <QueryClientProvider client={queryClient}>
          <AuthProvider isAuthed={context.user?.isAuthed ?? false}>
            <ThemeProvider defaultTheme="dark" storageKey="haveachat-theme">
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
    <html suppressHydrationWarning>
      <head>
        <HeadContent />
        {/* Prevent Flash of Wrong Theme */}
        <script
          dangerouslySetInnerHTML={{
            __html: `
              (function() {
                try {
                  const theme = localStorage.getItem('haveachat-theme');
                  if (theme === 'dark') {
                    document.documentElement.classList.add('dark');
                  } else if (theme === 'light') {
                    document.documentElement.classList.remove('dark');
                  }
                } catch (e) {}
              })();
            `,
          }}
        />
        <link
          rel="preload"
          href="https://fonts.googleapis.com/css2?family=Pacifico&display=swap"
          as="style"
        />
        <link
          rel="stylesheet"
          href="https://fonts.googleapis.com/css2?family=Pacifico&display=swap"
        />
        <noscript>
          <link
            rel="stylesheet"
            href="https://fonts.googleapis.com/css2?family=Pacifico&display=swap"
          />
        </noscript>
      </head>
      <body>
        {children}
        <Scripts />
      </body>
    </html>
  )
}
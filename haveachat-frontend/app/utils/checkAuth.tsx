import { createServerFn } from '@tanstack/react-start';
import { getCookie } from 'vinxi/http';

export const checkAuth = createServerFn().handler(async () => {
  const sessionCookie = getCookie('JSESSIONID');
	const csrfToken = getCookie('XSRF-TOKEN');
	console.log('CSRF Token from cookies:', csrfToken); 
  const isAuthenticated = Boolean(sessionCookie);
  return { isAuthenticated, csrfToken };
});
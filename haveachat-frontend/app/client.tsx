/// <reference types="vinxi/types/client" />
import { hydrateRoot } from 'react-dom/client';
import { StartClient } from '@tanstack/react-start';
import { AuthState, createRouter } from './router';

const authState: AuthState = {
  isAuthenticated: false,
  user: null,
};

const router = createRouter({ authState });

hydrateRoot(document, <StartClient router={router} />);
import { useQueryClient } from '@tanstack/react-query';
import { createContext, useContext, useEffect, useState } from 'react';
import { useLogOutMutation } from '../hooks/mutations/user/useLogOutMutation';
import { useMe } from '../hooks/queries/user/useMe';

interface AuthContextType {
  user: any | null; // change this once i know the shape
  login: () => Promise<void>;
  logout: () => void;
  userIsLoading: boolean;
}

const AuthContext = createContext<AuthContextType | undefined>(undefined);

export const AuthProvider = ({ children }) => {
  const queryClient = useQueryClient();
  const { data: user, isLoading: userIsLoading } = useMe();
  const logoutMutation = useLogOutMutation();

  const login = async () => {
    // Called after OAuth login completes on backend
    await queryClient.invalidateQueries({ queryKey: ['me'] });
  };

  const logout = () => logoutMutation.mutate();

  return (
    <AuthContext.Provider value={{ user, login, logout, userIsLoading }}>
      {!userIsLoading && children}
    </AuthContext.Provider>
  );
};

export const useAuth = () => {
  const context = useContext(AuthContext);
  if (!context) throw new Error('useAuth must be used within an AuthProvider');
  return context;
};
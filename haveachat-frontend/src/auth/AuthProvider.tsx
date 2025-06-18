import { UserDTO } from '@haveachat/api';
import { useMe } from '@haveachat/hooks/queries/user/useMe';
import { useLogOutMutation } from '@haveachat/hooks/mutations/user/useLogOutMutation';
import { useQueryClient } from '@tanstack/react-query';
import { createContext, useContext, useEffect, useState } from 'react';
import { useNavigate } from '@tanstack/react-router';

export type AuthState = {
  user: UserDTO | undefined;
  login: () => Promise<void>;
  logout: () => void;
  userIsLoading: boolean;
  userIsAuthed: boolean;
};

type AuthContextType = AuthState;

const AuthContext = createContext<AuthContextType | undefined>(undefined);

type AuthProviderProps = {
  children: React.ReactNode;
  isAuthed: boolean;
};

export const AuthProvider = ({ children, isAuthed = false }: AuthProviderProps) => {
  const queryClient = useQueryClient();
  const [userIsAuthed, setUserIsAuthed] = useState(isAuthed);
  const { data: user, isLoading } = useMe({ enabled: isAuthed });
	const navigate = useNavigate();

  const logOutMutation = useLogOutMutation();

  const login = async () => {
    // optional future login logic
  };

  const logout = async () => {
    setUserIsAuthed(false);
    queryClient.removeQueries({ queryKey: ['me'] });
    logOutMutation.mutate();
    window.location.href = '/login';
  };
  
  const authState: AuthState = {
    user,
    login,
    logout,
    userIsLoading: isLoading,
    userIsAuthed: userIsAuthed,
  };

  return (
    <AuthContext.Provider value={authState}>
      {children}
    </AuthContext.Provider>
  );
};

export const useAuth = (): AuthState => {
  const context = useContext(AuthContext);
  if (!context) throw new Error('useAuth must be used within an AuthProvider');
  return context;
};
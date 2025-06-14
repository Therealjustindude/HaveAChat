import { UserDTO } from '@haveachat/api';
import { useLogOutMutation } from '@haveachat/hooks/mutations/user/useLogOutMutation';
import { useMe } from '@haveachat/hooks/queries/user/useMe';
import { useQueryClient } from '@tanstack/react-query';
import { createContext, useContext } from 'react';

interface AuthContextType {
  user: UserDTO | undefined;
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
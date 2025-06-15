import { UserDTO } from '@haveachat/api';
import { useLogOutMutation } from '@haveachat/hooks/mutations/user/useLogOutMutation';
import { useMe } from '@haveachat/hooks/queries/user/useMe';
import { useQueryClient } from '@tanstack/react-query';
import { createContext, useContext, useEffect, useState } from 'react';

interface AuthContextType {
  user: UserDTO | undefined;
  login: () => Promise<void>;
  logout: () => void;
  userIsLoading: boolean;
}

const AuthContext = createContext<AuthContextType | undefined>(undefined);

export const AuthProvider = ({ children }) => {
  const queryClient = useQueryClient();
  // Track if we should try to fetch the user
  const [shouldFetchUser, setShouldFetchUser] = useState<boolean>(() => {
    if (typeof window !== 'undefined') {
      return !!localStorage.getItem('isLoggedIn');
    }
    return false; // On server, don't fetch user
  });

  // Only fetch user if we think we're logged in
  const { data: user, isLoading: userIsLoading, refetch } = useMe({ enabled: shouldFetchUser });

  const login = async () => {
    if (typeof window !== 'undefined') {
      localStorage.setItem('isLoggedIn', 'true');
    }
    setShouldFetchUser(true);
    await refetch();
  };

  const logout = () => {
    if (typeof window !== 'undefined') {
      localStorage.removeItem('isLoggedIn');
    }
    setShouldFetchUser(false);
    queryClient.removeQueries({ queryKey: ['me'] });
    // Optionally call your logout mutation
    useLogOutMutation().mutate();
  };

  // Optionally, clear flag if we get a 401 from /api/user
  useEffect(() => {
    if (!user && !userIsLoading && shouldFetchUser) {
      localStorage.removeItem('isLoggedIn');
      setShouldFetchUser(false);
    }
  }, [user, userIsLoading, shouldFetchUser]);
  
  return (
    <AuthContext.Provider value={{ user, login, logout, userIsLoading }}>
      {children}
    </AuthContext.Provider>
  );
};

export const useAuth = () => {
  const context = useContext(AuthContext);
  if (!context) throw new Error('useAuth must be used within an AuthProvider');
  return context;
};
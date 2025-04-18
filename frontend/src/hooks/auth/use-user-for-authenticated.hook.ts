import { useUser } from '@/hooks/auth';
import { User } from '@/types/common';

const useUserForAuthenticated = (): User => {
  const { user } = useUser();

  if (!user) {
    throw new Error('User is not authenticated');
  }

  return user;
};

export { useUserForAuthenticated };

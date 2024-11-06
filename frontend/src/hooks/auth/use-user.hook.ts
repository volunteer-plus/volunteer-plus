import { useAppSelector } from '@/hooks/store';
import { User } from '@/types/common';

const useUser = (): { user: User | null; isLoading: boolean } => {
  const { isLoading, user } = useAppSelector((state) => state.user);

  return {
    isLoading,
    user,
  };
};

export { useUser };

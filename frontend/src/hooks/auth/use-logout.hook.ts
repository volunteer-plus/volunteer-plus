import { useCallback } from 'react';
import { logout } from '@/slices/user';
import { useAppDispatch } from '@/hooks/store';

const useLogout = () => {
  const dispatch = useAppDispatch();

  return useCallback(() => {
    dispatch(logout());
  }, [dispatch]);
};

export { useLogout };

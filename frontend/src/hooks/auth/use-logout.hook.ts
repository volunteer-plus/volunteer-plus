import { useDispatch } from 'react-redux';
import { useCallback } from 'react';
import { clearUser } from '@/slices/user';

const useLogout = () => {
  const dispatch = useDispatch();

  return useCallback(() => {
    dispatch(clearUser());
  }, [dispatch]);
};

export { useLogout };

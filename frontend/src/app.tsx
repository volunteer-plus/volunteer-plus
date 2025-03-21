import { useEffect } from 'react';
import { RouterProvider } from 'react-router-dom';

import { useAppDispatch } from '@/hooks/store';
import { logout, restoreSession } from '@/slices/user';
import { router } from '@/router';

import './reset.css';
import './index.scss';
import { volunteerPlusApiService } from './services/common/volunteer-plus-api/volunteer-plus-api.service';

const App: React.FC = () => {
  const dispatch = useAppDispatch();

  useEffect(() => {
    dispatch(restoreSession());
  }, [dispatch]);

  useEffect(() => {
    const handleAccessTokenExpired = () => {
      dispatch(logout());
    };

    volunteerPlusApiService.addEventListener(
      'accessTokenExpired',
      handleAccessTokenExpired
    );

    return () => {
      volunteerPlusApiService.removeEventListener(
        'accessTokenExpired',
        handleAccessTokenExpired
      );
    };
  }, [dispatch]);

  return <RouterProvider router={router} />;
};

export { App };

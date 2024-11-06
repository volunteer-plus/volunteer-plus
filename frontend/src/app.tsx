import { useEffect } from 'react';
import { RouterProvider } from 'react-router-dom';

import { useAppDispatch } from '@/hooks/store';
import { restoreSession } from '@/slices/user';
import { router } from '@/router';

import './reset.css';
import './index.scss';

const App: React.FC = () => {
  const dispatch = useAppDispatch();

  useEffect(() => {
    dispatch(restoreSession());
  });

  return <RouterProvider router={router} />;
};

export { App };

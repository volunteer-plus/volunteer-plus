import { createBrowserRouter } from 'react-router-dom';

import { HomePage } from './components/common';
import { SignInPage, SignUpPage } from './components/auth';

const router = createBrowserRouter([
  {
    path: '',
    element: <HomePage />,
  },
  {
    path: 'sign-in',
    element: <SignInPage />,
  },
  {
    path: 'sign-up',
    element: <SignUpPage />,
  },
]);

export { router };

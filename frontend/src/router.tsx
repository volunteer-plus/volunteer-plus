import { createBrowserRouter, Outlet } from 'react-router-dom';

import {
  HomePage,
  PageFooter,
  PageHeader,
  PageLayout,
} from './components/common';
import { SignInPage, SignUpPage } from '@/components/auth';
import {
  AdminPageBody,
  AdminPageHeader,
  AdminPageLayout,
  AdminPageSidebar,
} from './components/admin';
import { BrigadesPage } from './components/brigades';

const router = createBrowserRouter([
  {
    element: (
      <PageLayout>
        <PageHeader />
        <Outlet />
        <PageFooter />
      </PageLayout>
    ),
    children: [
      {
        path: '',
        element: <HomePage />,
      },
    ],
  },
  {
    element: (
      <AdminPageLayout>
        <AdminPageHeader />
        <AdminPageBody>
          <AdminPageSidebar />
          <Outlet />
        </AdminPageBody>
      </AdminPageLayout>
    ),
    children: [
      {
        path: 'brigades',
        element: <BrigadesPage />,
      },
    ],
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

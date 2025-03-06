import { createBrowserRouter, Outlet } from 'react-router-dom';

import {
  HomePage,
  PageFooter,
  PageHeader,
  PageLayout,
  ProfilePage,
} from './components/common';
import { SignInPage, SignUpPage } from '@/components/auth';
import {
  AdminPageBody,
  AdminPageHeader,
  AdminPageLayout,
  AdminPageSidebar,
} from './components/admin';
import { BrigadesPage, MyBrigadePage } from './components/brigades';
import { ChatsPage } from './components/chats';
import {
  ServicemanRequestsPage,
  ServicemanOneRequestPage,
  VolunteerRequestsPage,
  VolunteerOneRequestPage,
} from './components/requests';
import {
  FundraisingActivitiesPage,
  SupportFundraisingPage,
  OneFundraisingActivityPage,
} from './components/fundraising';

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
      {
        path: 'support-fundraising/:id',
        element: <SupportFundraisingPage />,
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
      {
        path: 'my-brigade',
        element: <MyBrigadePage />,
      },
      {
        path: 'chats',
        element: <ChatsPage />,
      },
      {
        path: 'profile',
        element: <ProfilePage />,
      },
      {
        path: 'volunteer/requests/:tab',
        element: <VolunteerRequestsPage />,
      },
      {
        path: 'volunteer/request/:id',
        element: <VolunteerOneRequestPage />,
      },
      {
        path: 'serviceman/requests',
        element: <ServicemanRequestsPage />,
      },
      {
        path: 'serviceman/request/:id',
        element: <ServicemanOneRequestPage />,
      },
      {
        path: 'fundraising-activities',
        element: <FundraisingActivitiesPage />,
      },
      {
        path: 'fundraising-activity/:id',
        element: <OneFundraisingActivityPage />,
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

import React from 'react';

import { AdminPageContent, AdminPageTitle } from '@/components/admin';
import { Authenticated } from '@/components/auth';

const BareMyBrigadePage: React.FC = () => {
  return (
    <AdminPageContent>
      <AdminPageTitle>Моя бригада</AdminPageTitle>
      My brigade
    </AdminPageContent>
  );
};

const MyBrigadePage = Authenticated(BareMyBrigadePage, {
  allowedRoles: {
    brigadeAdmin: true,
  },
});

export { MyBrigadePage };

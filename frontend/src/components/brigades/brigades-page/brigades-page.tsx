import React from 'react';

import { Button } from '@/components/common';

import styles from './styles.module.scss';
import { AdminPageContent } from '@/components/admin';
import { Authenticated } from '@/components/auth';

const BareBrigadesPage: React.FC = () => {
  return (
    <AdminPageContent>
      <div>Brigades</div>
    </AdminPageContent>
  );
};

const BrigadesPage = Authenticated(BareBrigadesPage, {
  allowedRoles: {
    superAdmin: true,
  },
});

export { BrigadesPage };

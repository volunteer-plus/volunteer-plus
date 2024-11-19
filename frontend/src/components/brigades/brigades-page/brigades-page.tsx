import React, { useState } from 'react';

import { Button } from '@/components/common';

import { AdminPageContent, AdminPageTitle } from '@/components/admin';
import { Authenticated } from '@/components/auth';

import styles from './styles.module.scss';
import { AddBrigadeModal } from '../add-brigade-modal';

const BareBrigadesPage: React.FC = () => {
  const [isAddBrigadeModalOpen, setIsAddBrigadeModalOpen] = useState(false);

  return (
    <AdminPageContent>
      <div className={styles.header}>
        <AdminPageTitle>Бригади</AdminPageTitle>
        <Button onClick={() => setIsAddBrigadeModalOpen(true)}>
          Додати бригаду
        </Button>
      </div>
      <AddBrigadeModal
        isOpen={isAddBrigadeModalOpen}
        onClose={() => setIsAddBrigadeModalOpen(false)}
      />
    </AdminPageContent>
  );
};

const BrigadesPage = Authenticated(BareBrigadesPage, {
  allowedRoles: {
    superAdmin: true,
  },
});

export { BrigadesPage };

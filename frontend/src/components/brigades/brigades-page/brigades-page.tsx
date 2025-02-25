import React, { useEffect, useState } from 'react';

import {
  BarsLoader,
  Button,
  Pagination,
  TextInputField,
  PageTitle,
} from '@/components/common';
import { Authenticated } from '@/components/auth';
import { AdminPageContent } from '@/components/admin';
import { AddBrigadeModal, BrigadeListItem } from '@/components/brigades';

import styles from './styles.module.scss';

const BareBrigadesPage: React.FC = () => {
  const [isAddBrigadeModalOpen, setIsAddBrigadeModalOpen] = useState(false);
  const [isLoading, setIsLoading] = useState(true);

  useEffect(() => {
    setTimeout(() => {
      setIsLoading(false);
    }, 1000);
  }, []);

  return (
    <AdminPageContent className={styles.content}>
      <div className={styles.internalContent}>
        <div className={styles.header}>
          <PageTitle>Бригади</PageTitle>
          <Button onClick={() => setIsAddBrigadeModalOpen(true)}>
            Додати бригаду
          </Button>
        </div>
        <div className={styles.brigadesListAndSearch}>
          <TextInputField placeholder='Пошук' />
          {isLoading ? (
            <div className={styles.loaderContainer}>
              <BarsLoader size='50px' />
            </div>
          ) : (
            <>
              <div className={styles.brigadesList}>
                <BrigadeListItem />
                <BrigadeListItem />
                <BrigadeListItem />
                <BrigadeListItem />
                <BrigadeListItem />
              </div>
              <Pagination
                currentPage={1}
                totalPages={10}
                getPageUrl={() => '/'}
                className={styles.pagination}
              />
            </>
          )}
        </div>
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

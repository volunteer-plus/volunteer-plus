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
import { useAppDispatch, useAppSelector } from '@/hooks/store';
import { loadBrigades } from '@/slices/brigades';

import styles from './styles.module.scss';

const BareBrigadesPage: React.FC = () => {
  const dispatch = useAppDispatch();

  const { data, isLoading } = useAppSelector((state) => state.brigades);
  const [isAddBrigadeModalOpen, setIsAddBrigadeModalOpen] = useState(false);

  useEffect(() => {
    dispatch(loadBrigades());
  }, [dispatch]);

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
          {isLoading || !data ? (
            <div className={styles.loaderContainer}>
              <BarsLoader size='50px' />
            </div>
          ) : (
            <>
              <div className={styles.brigadesList}>
                {data.value.map((brigade) => (
                  <BrigadeListItem
                    key={brigade.id}
                    brigadeId={brigade.id}
                    brigadeName={brigade.name}
                  />
                ))}
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
        onAfterSubmit={() => setIsAddBrigadeModalOpen(false)}
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

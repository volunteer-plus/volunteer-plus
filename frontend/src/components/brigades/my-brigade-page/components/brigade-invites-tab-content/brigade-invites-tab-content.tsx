import { useEffect, useState } from 'react';
import { Form, Formik } from 'formik';
import {
  GrayContainer,
  TextInputField,
  Table,
  TableHead,
  TableBody,
  TableRow,
  TableHeaderCell,
  Pagination,
  FilterButton,
  Button,
  FormikMultiselectField,
} from '@/components/common';
import { AddInvitesModal } from '@/components/brigades';
import { useAppDispatch, useAppSelector } from '@/hooks/store';
import { loadMyBrigadeInvitesIfNotLoaded } from '@/slices/my-brigade-invites';

import { BrigadeInviteRow } from '../brigade-invite-row';
import styles from './styles.module.scss';

const BrigadeInvitesTabContent: React.FC = () => {
  const dispatch = useAppDispatch();

  const myBrigadeInvitesState = useAppSelector(
    (state) => state.myBrigadeInvites
  );

  const myBrigadeState = useAppSelector((state) => state.myBrigade);

  const [isAddInvitesModalOpen, setIsAddInvitesModalOpen] = useState(false);

  useEffect(() => {
    if (!myBrigadeState.data) {
      return;
    }

    dispatch(
      loadMyBrigadeInvitesIfNotLoaded({
        regimentCode: myBrigadeState.data.value.regimentCode,
      })
    );
  }, [myBrigadeState.data, dispatch]);

  return (
    <GrayContainer isUnderTabs className={styles.container}>
      <div className={styles.toolbar}>
        <div className={styles.toolbar}>
          <TextInputField placeholder='Пошук' className={styles.searchField} />
          <FilterButton>
            <Formik initialValues={{ status: [] }} onSubmit={() => {}}>
              <Form>
                <FormikMultiselectField
                  placeholder='Статус'
                  label='Статус'
                  name='status'
                  options={[
                    { label: 'Нове', value: 'new' },
                    { label: 'Відкликане', value: 'canceled' },
                    { label: 'Прийняте', value: 'accepted' },
                  ]}
                />
              </Form>
            </Formik>
          </FilterButton>
        </div>
        <Button
          onClick={() => setIsAddInvitesModalOpen(true)}
          disabled={!myBrigadeState.data}
        >
          Додати запрошення
        </Button>
      </div>
      <Table className={styles.table}>
        <TableHead>
          <TableRow>
            <TableHeaderCell>Код</TableHeaderCell>
            <TableHeaderCell>Статус</TableHeaderCell>
            <TableHeaderCell>Дата створення</TableHeaderCell>
            <TableHeaderCell>Дата активації</TableHeaderCell>
            <TableHeaderCell>Email члена бригади</TableHeaderCell>
            <TableHeaderCell></TableHeaderCell>
          </TableRow>
        </TableHead>
        <TableBody>
          {myBrigadeInvitesState.data?.value.map((invite) => (
            <BrigadeInviteRow key={invite.id} data={invite} />
          ))}
        </TableBody>
      </Table>
      <Pagination currentPage={1} totalPages={10} getPageUrl={() => '/'} />
      <AddInvitesModal
        isOpen={isAddInvitesModalOpen}
        onClose={() => setIsAddInvitesModalOpen(false)}
      />
    </GrayContainer>
  );
};

export { BrigadeInvitesTabContent };

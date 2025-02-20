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
import styles from './styles.module.scss';

import { BrigadeInviteRow } from '../brigade-invite-row';
import { useState } from 'react';
import { AddInvitesModal } from '@/components/brigades/add-invites-modal';

const BrigadeInvitesTabContent: React.FC = () => {
  const [isAddInvitesModalOpen, setIsAddInvitesModalOpen] = useState(false);

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
                  value={[]}
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
        <Button onClick={() => setIsAddInvitesModalOpen(true)}>
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
          <BrigadeInviteRow />
          <BrigadeInviteRow />
          <BrigadeInviteRow />
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

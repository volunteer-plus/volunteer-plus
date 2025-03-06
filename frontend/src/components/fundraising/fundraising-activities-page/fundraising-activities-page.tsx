import { useState } from 'react';

import { AdminPageContent } from '@/components/admin';
import { Authenticated } from '@/components/auth';
import {
  Button,
  FilterButton,
  GrayContainer,
  PageTitle,
  Pagination,
  ProgressTag,
  RightChevronButton,
  Table,
  TableActionsCell,
  TableBody,
  TableDataCell,
  TableHead,
  TableHeaderCell,
  TableRow,
  TextInputField,
} from '@/components/common';
import { AddFundraisingModal } from '@/components/fundraising';
import { formatDateAndTime } from '@/helpers/common';

import { FilterForm } from './components';
import styles from './styles.module.scss';
import { Link } from 'react-router-dom';

const BareFundraisingActivitiesPage: React.FC = () => {
  const [isAddFundraisingModalOpen, setIsAddFundraisingModalOpen] =
    useState(false);

  return (
    <AdminPageContent>
      <PageTitle className={styles.pageTitle}>Збори</PageTitle>
      <GrayContainer>
        <div className={styles.toolbar}>
          <div className={styles.searchAndFilter}>
            <TextInputField placeholder='Пошук' className={styles.search} />
            <FilterButton>
              <FilterForm />
            </FilterButton>
          </div>
          <Button onClick={() => setIsAddFundraisingModalOpen(true)}>
            Додати збір
          </Button>
        </div>
        <Table className={styles.table}>
          <TableHead>
            <TableRow>
              <TableHeaderCell>Номер</TableHeaderCell>
              <TableHeaderCell>Збір</TableHeaderCell>
              <TableHeaderCell>Категорія</TableHeaderCell>
              <TableHeaderCell>Статус</TableHeaderCell>
              <TableHeaderCell>Дата створення</TableHeaderCell>
              <TableHeaderCell></TableHeaderCell>
            </TableRow>
          </TableHead>
          <TableBody>
            <TableRow>
              <TableDataCell>12312</TableDataCell>
              <TableDataCell>Бронежелети</TableDataCell>
              <TableDataCell>Амуніція</TableDataCell>
              <TableDataCell>
                <ProgressTag progress={0.75} size='medium'>
                  Іде збір
                </ProgressTag>
              </TableDataCell>
              <TableDataCell>{formatDateAndTime(new Date())}</TableDataCell>
              <TableActionsCell>
                <RightChevronButton as={Link} to='/fundraising-activity/123' />
              </TableActionsCell>
            </TableRow>
            <TableRow>
              <TableDataCell>12312</TableDataCell>
              <TableDataCell>Бронежелети</TableDataCell>
              <TableDataCell>Амуніція</TableDataCell>
              <TableDataCell>
                <ProgressTag progress={1} size='medium'>
                  Збір завершено
                </ProgressTag>
              </TableDataCell>
              <TableDataCell>{formatDateAndTime(new Date())}</TableDataCell>
              <TableActionsCell>
                <RightChevronButton as={Link} to='/fundraising-activity/123' />
              </TableActionsCell>
            </TableRow>
            <TableRow>
              <TableDataCell>12312</TableDataCell>
              <TableDataCell>Бронежелети</TableDataCell>
              <TableDataCell>Амуніція</TableDataCell>
              <TableDataCell>
                <ProgressTag progress={null} size='medium'>
                  Завантажено звіт
                </ProgressTag>
              </TableDataCell>
              <TableDataCell>{formatDateAndTime(new Date())}</TableDataCell>
              <TableActionsCell>
                <RightChevronButton as={Link} to='/fundraising-activity/123' />
              </TableActionsCell>
            </TableRow>
          </TableBody>
        </Table>
        <Pagination currentPage={5} getPageUrl={() => '#'} totalPages={100} />
      </GrayContainer>
      <AddFundraisingModal
        isOpen={isAddFundraisingModalOpen}
        onClose={() => setIsAddFundraisingModalOpen(false)}
      />
    </AdminPageContent>
  );
};

const FundraisingActivitiesPage = Authenticated(BareFundraisingActivitiesPage, {
  allowedRoles: {
    volunteer: true,
  },
});

export { FundraisingActivitiesPage };

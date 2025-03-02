import { useState } from 'react';
import { AdminPageContent } from '@/components/admin';
import { Authenticated } from '@/components/auth';
import {
  Button,
  FilterButton,
  GrayContainer,
  PageTitle,
  Pagination,
  RightChevronButton,
  Table,
  TableActionsCell,
  TableBody,
  TableDataCell,
  TableHead,
  TableHeaderCell,
  TableRow,
  Tag,
  TextInputField,
} from '@/components/common';
import { AddRequestModal } from '@/components/requests';

import styles from './styles.module.scss';
import { FilterForm } from './components';
import { Link } from 'react-router-dom';

const BareServicemanRequestsPage: React.FC = () => {
  const [isAddRequestModalOpen, setIsAddRequestModalOpen] = useState(false);

  return (
    <AdminPageContent>
      <PageTitle className={styles.pageTitle}>Запити</PageTitle>
      <GrayContainer isUnderTabs>
        <div className={styles.toolbar}>
          <div className={styles.searchAndFilter}>
            <TextInputField placeholder='Пошук' className={styles.search} />
            <FilterButton>
              <FilterForm />
            </FilterButton>
          </div>
          <Button onClick={() => setIsAddRequestModalOpen(true)}>
            Додати запит
          </Button>
        </div>
        <Table className={styles.table}>
          <TableHead>
            <TableRow>
              <TableHeaderCell>Номер</TableHeaderCell>
              <TableHeaderCell>Запит</TableHeaderCell>
              <TableHeaderCell>Категорія</TableHeaderCell>
              <TableHeaderCell>Статус</TableHeaderCell>
              <TableHeaderCell>Нові пропозиції</TableHeaderCell>
              <TableHeaderCell></TableHeaderCell>
            </TableRow>
          </TableHead>
          <TableBody>
            <TableRow>
              <TableDataCell>123</TableDataCell>
              <TableDataCell>Бронежелети</TableDataCell>
              <TableDataCell>Амуніція</TableDataCell>
              <TableDataCell>
                <Tag color='var(--color-olive-300)' size='medium'>
                  Новий
                </Tag>
              </TableDataCell>
              <TableDataCell>123</TableDataCell>
              <TableActionsCell>
                <Link to='/serviceman/requests/123'>
                  <RightChevronButton />
                </Link>
              </TableActionsCell>
            </TableRow>
            <TableRow>
              <TableDataCell>123</TableDataCell>
              <TableDataCell>Бронежелети</TableDataCell>
              <TableDataCell>Амуніція</TableDataCell>
              <TableDataCell>
                <Tag color='var(--color-olive-500)' size='medium'>
                  Виконується
                </Tag>
              </TableDataCell>
              <TableDataCell></TableDataCell>
              <TableActionsCell>
                <Link to='/serviceman/requests/123'>
                  <RightChevronButton />
                </Link>
              </TableActionsCell>
            </TableRow>
            <TableRow>
              <TableDataCell>123</TableDataCell>
              <TableDataCell>Бронежелети</TableDataCell>
              <TableDataCell>Амуніція</TableDataCell>
              <TableDataCell>
                <Tag color='var(--color-olive-700)' size='medium'>
                  Виконаний
                </Tag>
              </TableDataCell>
              <TableDataCell></TableDataCell>
              <TableActionsCell>
                <Link to='/serviceman/requests/123'>
                  <RightChevronButton />
                </Link>
              </TableActionsCell>
            </TableRow>
            <TableRow>
              <TableDataCell>123</TableDataCell>
              <TableDataCell>Бронежелети</TableDataCell>
              <TableDataCell>Амуніція</TableDataCell>
              <TableDataCell>
                <Tag color='var(--color-gray-300)' size='medium'>
                  Скасований
                </Tag>
              </TableDataCell>
              <TableDataCell></TableDataCell>
              <TableActionsCell>
                <Link to='/serviceman/requests/123'>
                  <RightChevronButton />
                </Link>
              </TableActionsCell>
            </TableRow>
          </TableBody>
        </Table>
        <Pagination
          currentPage={1}
          totalPages={10}
          getPageUrl={() => '/'}
          className={styles.pagination}
        />
      </GrayContainer>
      <AddRequestModal
        isOpen={isAddRequestModalOpen}
        onClose={() => setIsAddRequestModalOpen(false)}
      />
    </AdminPageContent>
  );
};

const ServicemanRequestsPage = Authenticated(BareServicemanRequestsPage, {
  allowedRoles: {
    serviceman: true,
  },
});

export { ServicemanRequestsPage };

import classNames from 'classnames';
import { Link } from 'react-router-dom';

import {
  FilterButton,
  GrayContainer,
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
} from '@/components/common';
import { formatDateAndTime } from '@/helpers/common';

import { FilterForm } from './components';
import styles from './styles.module.scss';

type Props = Omit<
  React.ComponentPropsWithoutRef<typeof GrayContainer>,
  'children'
>;

const MyRequestsTabContent: React.FC<Props> = ({ className, ...props }) => {
  return (
    <GrayContainer
      {...props}
      className={classNames(styles.root, className)}
      isUnderTabs
    >
      <FilterButton className={styles.filterButton}>
        <FilterForm />
      </FilterButton>
      <Table className={styles.table}>
        <TableHead>
          <TableRow>
            <TableHeaderCell>Номер</TableHeaderCell>
            <TableHeaderCell>Запит</TableHeaderCell>
            <TableHeaderCell>Категорія</TableHeaderCell>
            <TableHeaderCell>Бригада</TableHeaderCell>
            <TableHeaderCell>Статус</TableHeaderCell>
            <TableHeaderCell>Дата створення</TableHeaderCell>
            <TableHeaderCell></TableHeaderCell>
          </TableRow>
        </TableHead>
        <TableBody>
          <TableRow>
            <TableDataCell>12312</TableDataCell>
            <TableDataCell>Бронежелети</TableDataCell>
            <TableDataCell>Ауніція</TableDataCell>
            <TableDataCell>3 ОШБ</TableDataCell>
            <TableDataCell>
              <Tag color='var(--color-olive-300)' size='medium'>
                Новий
              </Tag>
            </TableDataCell>
            <TableDataCell>{formatDateAndTime(new Date())}</TableDataCell>
            <TableActionsCell>
              <RightChevronButton as={Link} to='/volunteer/request/1234' />
            </TableActionsCell>
          </TableRow>
          <TableRow>
            <TableDataCell>12312</TableDataCell>
            <TableDataCell>Бронежелети</TableDataCell>
            <TableDataCell>Ауніція</TableDataCell>
            <TableDataCell>3 ОШБ</TableDataCell>
            <TableDataCell>
              <Tag color='var(--color-olive-300)' size='medium'>
                Новий
              </Tag>
            </TableDataCell>
            <TableDataCell>{formatDateAndTime(new Date())}</TableDataCell>
            <TableActionsCell>
              <RightChevronButton as={Link} to='/volunteer/request/1234' />
            </TableActionsCell>
          </TableRow>
          <TableRow>
            <TableDataCell>12312</TableDataCell>
            <TableDataCell>Бронежелети</TableDataCell>
            <TableDataCell>Ауніція</TableDataCell>
            <TableDataCell>3 ОШБ</TableDataCell>
            <TableDataCell>
              <Tag color='var(--color-olive-700)' size='medium'>
                Виконаний
              </Tag>
            </TableDataCell>
            <TableDataCell>{formatDateAndTime(new Date())}</TableDataCell>
            <TableActionsCell>
              <RightChevronButton as={Link} to='/volunteer/request/1234' />
            </TableActionsCell>
          </TableRow>
        </TableBody>
      </Table>
      <Pagination currentPage={1} getPageUrl={() => '#'} totalPages={100} />
    </GrayContainer>
  );
};

export { MyRequestsTabContent };

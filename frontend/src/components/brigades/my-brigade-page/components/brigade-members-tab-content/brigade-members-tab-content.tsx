import {
  CheckboxAndLabelContainer,
  FieldLabel,
  GrayContainer,
  TextInputField,
  Toggle,
  Table,
  TableHead,
  TableBody,
  TableRow,
  TableHeaderCell,
  TableDataCell,
  TableUserCell,
  Pagination,
  TableActionsCell,
  TableAction,
} from '@/components/common';
import styles from './styles.module.scss';

const BrigadeMembersTabContent: React.FC = () => {
  return (
    <GrayContainer isUnderTabs className={styles.container}>
      <div className={styles.toolbar}>
        <TextInputField placeholder='Пошук' className={styles.searchField} />
        <CheckboxAndLabelContainer>
          <Toggle />
          <FieldLabel>Показувати деактивованих</FieldLabel>
        </CheckboxAndLabelContainer>
      </div>
      <Table className={styles.table}>
        <TableHead>
          <TableRow>
            <TableHeaderCell>Користувач</TableHeaderCell>
            <TableHeaderCell>Email</TableHeaderCell>
            <TableHeaderCell>Кількість запитів</TableHeaderCell>
            <TableHeaderCell></TableHeaderCell>
          </TableRow>
        </TableHead>
        <TableBody>
          <TableRow>
            <TableUserCell />
            <TableDataCell>petrenko@gmail.com</TableDataCell>
            <TableDataCell>123</TableDataCell>
            <TableActionsCell>
              <TableAction>delete</TableAction>
            </TableActionsCell>
          </TableRow>
          <TableRow>
            <TableUserCell />
            <TableDataCell>petrenko@gmail.com</TableDataCell>
            <TableDataCell>123</TableDataCell>
            <TableActionsCell>
              <TableAction>delete</TableAction>
            </TableActionsCell>
          </TableRow>
        </TableBody>
      </Table>
      <Pagination currentPage={1} totalPages={10} getPageUrl={() => '/'} />
    </GrayContainer>
  );
};

export { BrigadeMembersTabContent };

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
  Pagination,
} from '@/components/common';
import styles from './styles.module.scss';
import { BrigadeMemberRow } from '../brigade-member-row';

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
          <BrigadeMemberRow />
          <BrigadeMemberRow />
          <BrigadeMemberRow />
          <BrigadeMemberRow />
        </TableBody>
      </Table>
      <Pagination currentPage={1} totalPages={10} getPageUrl={() => '/'} />
    </GrayContainer>
  );
};

export { BrigadeMembersTabContent };

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
import { useAppSelector } from '@/hooks/store';

import styles from './styles.module.scss';
import { BrigadeMemberRow } from '../brigade-member-row';

const BrigadeMembersTabContent: React.FC = () => {
  const myBrigadeState = useAppSelector((state) => state.myBrigade);

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
          {myBrigadeState.data?.value.militaryPersonnel.map((person) => (
            <BrigadeMemberRow key={person.id} data={person} />
          ))}
        </TableBody>
      </Table>
      <Pagination currentPage={1} totalPages={10} getPageUrl={() => '/'} />
    </GrayContainer>
  );
};

export { BrigadeMembersTabContent };

import { Avatar, TableDataCell } from '@/components/common';

import styles from './styles.module.scss';

type Props = Omit<React.ComponentProps<typeof TableDataCell>, 'children'>;

const TableUserCell: React.FC<Props> = (props) => {
  return (
    <TableDataCell {...props}>
      <div className={styles.cellContent}>
        <Avatar size='36px' />
        <span>Петренко Петро Петрович</span>
      </div>
    </TableDataCell>
  );
};

export { TableUserCell };

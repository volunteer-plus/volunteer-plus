import { Avatar, TableDataCell } from '@/components/common';

import styles from './styles.module.scss';

type Props = Omit<React.ComponentProps<typeof TableDataCell>, 'children'> & {
  name: string;
};

const TableUserCell: React.FC<Props> = ({ name, ...props }) => {
  return (
    <TableDataCell {...props}>
      <div className={styles.cellContent}>
        <Avatar size='36px' />
        <span>{name}</span>
      </div>
    </TableDataCell>
  );
};

export { TableUserCell };

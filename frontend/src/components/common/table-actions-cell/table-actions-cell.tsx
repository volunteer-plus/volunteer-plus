import { TableDataCell } from '@/components/common';

import styles from './styles.module.scss';

const TableActionsCell: React.FC<
  React.ComponentProps<typeof TableDataCell>
> = ({ children, ...props }) => {
  return (
    <TableDataCell {...props}>
      <div className={styles.cellContent}>{children}</div>
    </TableDataCell>
  );
};

export { TableActionsCell };

import {
  ConfirmationModal,
  TableAction,
  TableActionsCell,
  TableDataCell,
  TableRow,
  TableUserCell,
} from '@/components/common';
import { getFullName } from '@/helpers/user';
import { useState } from 'react';

interface Props {
  data: {
    firstName: string;
    lastName: string;
    requestsCount?: number;
  };
}

const BrigadeMemberRow: React.FC<Props> = ({ data }) => {
  const [isDeactivateModalOpen, setIsDeactivateModalOpen] = useState(false);

  return (
    <TableRow>
      <TableUserCell name={getFullName(data)} />
      <TableDataCell>a@a.com</TableDataCell>
      <TableDataCell>{data.requestsCount ?? '-'}</TableDataCell>
      <TableActionsCell>
        <TableAction onClick={() => setIsDeactivateModalOpen(true)}>
          delete
        </TableAction>
      </TableActionsCell>
      <ConfirmationModal
        isOpen={isDeactivateModalOpen}
        onCancel={() => setIsDeactivateModalOpen(false)}
        title='Деактивувати користувача'
        cancelText='Скасувати'
        confirmText='Так, деактивувати'
      >
        Ви впевнені, що хочете деактивувати користувача?
      </ConfirmationModal>
    </TableRow>
  );
};

export { BrigadeMemberRow };

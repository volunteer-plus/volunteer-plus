import {
  ConfirmationModal,
  TableAction,
  TableActionsCell,
  TableDataCell,
  TableRow,
  TableUserCell,
} from '@/components/common';
import { useState } from 'react';

const BrigadeMemberRow = () => {
  const [isDeactivateModalOpen, setIsDeactivateModalOpen] = useState(false);

  return (
    <TableRow>
      <TableUserCell />
      <TableDataCell>petrenko@gmail.com</TableDataCell>
      <TableDataCell>123</TableDataCell>
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

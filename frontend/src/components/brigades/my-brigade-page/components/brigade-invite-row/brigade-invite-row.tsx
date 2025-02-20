import {
  ConfirmationModal,
  TableAction,
  TableActionsCell,
  TableDataCell,
  TableRow,
} from '@/components/common';
import { useState } from 'react';
import { InviteStatusTag } from '../invite-status-tag';

const BrigadeInviteRow = () => {
  const [isCancelModalOpen, setIsCancelModalOpen] = useState(false);

  return (
    <TableRow>
      <TableDataCell>2323232323</TableDataCell>
      <TableDataCell>
        <InviteStatusTag status='accepted' size='medium' />
      </TableDataCell>
      <TableDataCell>12:40 12.01.2025</TableDataCell>
      <TableDataCell>12:40 12.01.2025</TableDataCell>
      <TableDataCell>petrenko@gmail.com</TableDataCell>
      <TableActionsCell>
        <TableAction onClick={() => setIsCancelModalOpen(true)}>
          delete
        </TableAction>
      </TableActionsCell>
      <ConfirmationModal
        isOpen={isCancelModalOpen}
        onCancel={() => setIsCancelModalOpen(false)}
        title='Відкликати запрошення'
        cancelText='Скасувати'
        confirmText='Так, відкликати'
      >
        Ви впевнені, що хочете відкликати запрошеня?
      </ConfirmationModal>
    </TableRow>
  );
};

export { BrigadeInviteRow };

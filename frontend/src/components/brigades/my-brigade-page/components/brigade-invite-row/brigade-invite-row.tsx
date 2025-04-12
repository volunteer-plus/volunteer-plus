import { useState } from 'react';
import {
  ConfirmationModal,
  SecretKeyField,
  TableAction,
  TableActionsCell,
  TableDataCell,
  TableRow,
} from '@/components/common';
import { InviteStatusTag } from '../invite-status-tag';

type Props = {
  data: {
    code: string;
    executed: boolean;
    servicemanEmail?: string | null;
  };
};

const BrigadeInviteRow: React.FC<Props> = ({ data }) => {
  const [isCancelModalOpen, setIsCancelModalOpen] = useState(false);

  return (
    <TableRow>
      <TableDataCell>
        <SecretKeyField value={data.code} />
      </TableDataCell>
      <TableDataCell>
        <InviteStatusTag
          status={data.executed ? 'accepted' : 'new'}
          size='medium'
        />
      </TableDataCell>
      <TableDataCell>12:40 12.01.2025</TableDataCell>
      <TableDataCell>12:40 12.01.2025</TableDataCell>
      <TableDataCell>{data.servicemanEmail || '-'}</TableDataCell>
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

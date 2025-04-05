import {
  Button,
  GrayContainer,
  Pagination,
  SecretKeyField,
  Table,
  TableAction,
  TableActionsCell,
  TableBody,
  TableDataCell,
  TableHead,
  TableHeaderCell,
  TableRow,
} from '@/components/common';

import styles from './styles.module.scss';
import { useState } from 'react';
import { ExportModal } from '../export-modal';

interface Props {
  onClose: () => void;
  data: { id: number; code: string }[];
}

const ListModalContent: React.FC<Props> = ({ onClose, data }) => {
  const [isExportModalOpen, setIsExportModalOpen] = useState(false);

  return (
    <>
      <div className={styles.exportButtonContainer}>
        <Button
          colorSchema='gray'
          type='button'
          onClick={() => setIsExportModalOpen(true)}
        >
          Експортувати
        </Button>
      </div>
      <GrayContainer>
        <Table className={styles.table}>
          <TableHead>
            <TableRow>
              <TableHeaderCell>Код</TableHeaderCell>
              <TableHeaderCell></TableHeaderCell>
            </TableRow>
          </TableHead>
          <TableBody>
            {data.map((invite) => {
              return (
                <TableRow key={invite.id}>
                  <TableDataCell>
                    <SecretKeyField value={invite.code} />
                  </TableDataCell>
                  <TableActionsCell>
                    <TableAction>delete</TableAction>
                  </TableActionsCell>
                </TableRow>
              );
            })}
          </TableBody>
        </Table>
        <Pagination
          currentPage={1}
          totalPages={10}
          getPageUrl={() => '/'}
          className={styles.pagination}
        />
      </GrayContainer>
      <div className={styles.closeButtonContainer}>
        <Button
          colorSchema='gray'
          variant='outlined'
          type='button'
          onClick={() => onClose()}
        >
          Закрити
        </Button>
      </div>
      <ExportModal
        isOpen={isExportModalOpen}
        onClose={() => setIsExportModalOpen(false)}
      />
    </>
  );
};

export { ListModalContent };

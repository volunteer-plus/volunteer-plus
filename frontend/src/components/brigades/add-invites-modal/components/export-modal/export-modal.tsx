import {
  Button,
  Modal,
  ModalCloseButton,
  ModalContainer,
  ModalTitle,
  SelectField,
} from '@/components/common';

import styles from './styles.module.scss';
import { EXPORT_FORMAT_OPTIONS } from './constants';
import { ExportFormat } from './enums';
import { useState } from 'react';

interface Props {
  isOpen: boolean;
  onClose: () => void;
}

const ExportModal: React.FC<Props> = ({ isOpen, onClose }) => {
  const [exportFormat, setExportFormat] = useState(ExportFormat.PDF);

  return (
    <Modal isOpen={isOpen} onClose={onClose}>
      <ModalContainer width='400px'>
        <ModalTitle>Експортувати запрошення</ModalTitle>
        <ModalCloseButton onClick={() => onClose()} />
        <SelectField
          label='Формат'
          isRequired
          options={EXPORT_FORMAT_OPTIONS}
          value={exportFormat}
          onChange={(value) => setExportFormat(value as ExportFormat)}
          placeholder='Оберіть формат'
        />
        <div className={styles.actions}>
          <Button
            variant='outlined'
            colorSchema='gray'
            type='button'
            onClick={() => onClose()}
          >
            Скасувати
          </Button>
          <Button type='button'>Експортувати</Button>
        </div>
      </ModalContainer>
    </Modal>
  );
};

export { ExportModal };

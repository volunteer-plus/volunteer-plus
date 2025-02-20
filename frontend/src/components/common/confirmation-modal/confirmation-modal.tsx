import {
  Button,
  Modal,
  ModalCloseButton,
  ModalContainer,
  ModalTitle,
} from '@/components/common';

import styles from './styles.module.scss';

interface Props {
  title?: React.ReactNode;
  children?: React.ReactNode;
  isOpen: boolean;
  onConfirm?: () => void;
  onCancel: () => void;
  confirmText?: string;
  cancelText?: string;
}

const ConfirmationModal: React.FC<Props> = ({
  title,
  children,
  onConfirm,
  onCancel,
  isOpen,
  confirmText = 'Підтвердити',
  cancelText = 'Скасувати',
}) => {
  return (
    <Modal isOpen={isOpen} onClose={onCancel}>
      <ModalContainer>
        <ModalCloseButton onClick={onCancel} />
        {title && <ModalTitle>{title}</ModalTitle>}
        <div className={styles.text}>{children}</div>
        <div className={styles.buttons}>
          <Button onClick={onCancel} variant='outlined' colorSchema='gray'>
            {cancelText}
          </Button>
          <Button onClick={onConfirm} variant='filled' colorSchema='olive'>
            {confirmText}
          </Button>
        </div>
      </ModalContainer>
    </Modal>
  );
};

export { ConfirmationModal };

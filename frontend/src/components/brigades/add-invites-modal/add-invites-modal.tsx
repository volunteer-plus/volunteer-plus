import { useState } from 'react';
import {
  Modal,
  ModalCloseButton,
  ModalContainer,
  ModalTitle,
} from '@/components/common';
import { FormModalContent, ListModalContent } from './components';

interface Props {
  isOpen: boolean;
  onClose: () => void;
}

const AddInvitesModal: React.FC<Props> = ({ isOpen, onClose }) => {
  const [isFormSubmitted, setIsFormSubmitted] = useState(false);

  return (
    <Modal
      isOpen={isOpen}
      onClose={onClose}
      onOutAnimationEnd={() => setIsFormSubmitted(false)}
    >
      <ModalContainer>
        <ModalTitle>Додати запрошення</ModalTitle>
        <ModalCloseButton onClick={() => onClose()} />
        {isFormSubmitted ? (
          <ListModalContent onClose={onClose} />
        ) : (
          <FormModalContent
            onSubmit={() => setIsFormSubmitted(true)}
            onCancel={() => onClose()}
          />
        )}
      </ModalContainer>
    </Modal>
  );
};

export { AddInvitesModal };

import { useState } from 'react';
import {
  Modal,
  ModalCloseButton,
  ModalContainer,
  ModalTitle,
} from '@/components/common';
import { brigadesService } from '@/services/brigades/brigades';

import { FormModalContent, ListModalContent } from './components';

interface Props {
  isOpen: boolean;
  onClose: () => void;
}

const AddInvitesModal: React.FC<Props> = ({ isOpen, onClose }) => {
  const [isFormSubmitted, setIsFormSubmitted] = useState(false);

  const addInvites = async (count: number) => {
    const receivedInvites = await brigadesService.createBrigadeInvites({
      brigadeRegimentCode: '123',
      count,
    });
  };

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
            onSubmit={async ({ invitesNumber }) => {
              await addInvites(Number(invitesNumber));
            }}
            onCancel={() => onClose()}
          />
        )}
      </ModalContainer>
    </Modal>
  );
};

export { AddInvitesModal };

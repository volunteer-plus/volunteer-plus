import { useEffect, useState } from 'react';
import {
  Modal,
  ModalCloseButton,
  ModalContainer,
  ModalTitle,
} from '@/components/common';
import { BrigadeInvite, brigadesService } from '@/services/brigades/brigades';
import { loadMyBrigadeIfNotLoaded } from '@/slices/my-brigade';
import { useAppDispatch, useAppSelector } from '@/hooks/store';

import { FormModalContent, ListModalContent } from './components';
import { reloadMyBrigadeInvites } from '@/slices/my-brigade-invites';

interface Props {
  isOpen: boolean;
  onClose: () => void;
}

const AddInvitesModal: React.FC<Props> = ({ isOpen, onClose }) => {
  const dispatch = useAppDispatch();

  const myBrigadeState = useAppSelector((state) => state.myBrigade);
  const [createdInvites, setCreatedInvites] = useState<BrigadeInvite[] | null>(
    null
  );

  useEffect(() => {
    dispatch(loadMyBrigadeIfNotLoaded());
  }, [dispatch]);

  const addInvites = async (count: number) => {
    if (!myBrigadeState.data) {
      return;
    }

    const receivedInvites = await brigadesService.createBrigadeInvites({
      brigadeRegimentCode: myBrigadeState.data.value.regimentCode,
      count,
    });

    setCreatedInvites(receivedInvites);
    dispatch(reloadMyBrigadeInvites());
  };

  return (
    <Modal
      isOpen={isOpen}
      onClose={onClose}
      onOutAnimationEnd={() => setCreatedInvites(null)}
    >
      <ModalContainer>
        <ModalTitle>Додати запрошення</ModalTitle>
        <ModalCloseButton onClick={() => onClose()} />
        {createdInvites ? (
          <ListModalContent onClose={onClose} data={createdInvites} />
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

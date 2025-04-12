import {
  BarsLoader,
  Modal,
  ModalCloseButton,
  ModalContainer,
  ModalTitle,
} from '@/components/common';
import { fundraisingService } from '@/services/fundraising';
import { LiqpayCheckoutButtonVariables } from '@/types/fundraising';
import { useCallback, useEffect, useState } from 'react';
import { LiqpayCheckoutButton } from '../liqpay-checkout-button';
import styles from './styles.module.scss';

type Props = Pick<React.ComponentProps<typeof Modal>, 'isOpen' | 'onClose'> & {
  amount?: number | null;
};

const SupportFundraisingModal = ({ isOpen, onClose, amount }: Props) => {
  const [liqpayCheckoutButtonVariables, setLiqpayCheckoutButtonVariables] =
    useState<LiqpayCheckoutButtonVariables | null>(null);

  const createPayOrder = useCallback(async (_amount: number) => {
    const receivedLiqpayCheckoutButtonVariables =
      await fundraisingService.createDonationLiqPayOrder({
        amount: _amount,
      });

    setLiqpayCheckoutButtonVariables(receivedLiqpayCheckoutButtonVariables);
  }, []);

  useEffect(() => {
    if (!amount) {
      return;
    }

    createPayOrder(amount);
  }, [amount, createPayOrder]);

  useEffect(() => {
    if (!isOpen) {
      setLiqpayCheckoutButtonVariables(null);
    }
  }, [isOpen]);

  return (
    <Modal isOpen={isOpen} onClose={onClose}>
      <ModalContainer>
        <ModalCloseButton onClick={onClose} />
        <ModalTitle>Підтримати збір</ModalTitle>
        {liqpayCheckoutButtonVariables && (
          <LiqpayCheckoutButton {...liqpayCheckoutButtonVariables} />
        )}
        {!liqpayCheckoutButtonVariables && (
          <div className={styles.loaderContainer}>
            <BarsLoader size='36px' />
          </div>
        )}
      </ModalContainer>
    </Modal>
  );
};

export { SupportFundraisingModal };

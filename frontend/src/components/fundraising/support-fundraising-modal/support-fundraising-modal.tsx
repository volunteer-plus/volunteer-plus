import {
  Button,
  FormikTextInputField,
  Modal,
  ModalCloseButton,
  ModalContainer,
  ModalTitle,
} from '@/components/common';
import { fundraisingService } from '@/services/fundraising';
import { LiqpayCheckoutButtonVariables } from '@/types/fundraising';
import { Yup } from '@/yup';
import { Form, Formik } from 'formik';
import { useEffect, useState } from 'react';
import { LiqpayCheckoutButton } from '../liqpay-checkout-button';

type Props = Pick<React.ComponentProps<typeof Modal>, 'isOpen' | 'onClose'>;

interface FormValues {
  amount: string;
}

const FORM_INITIAL_VALUES: FormValues = {
  amount: '',
};

const FORM_VALIDATION_SCHEMA = Yup.object().shape({
  amount: Yup.number().positive().required().label('Сума'),
});

const SupportFundraisingModal = ({ isOpen, onClose }: Props) => {
  const [liqpayCheckoutButtonVariables, setLiqpayCheckoutButtonVariables] =
    useState<LiqpayCheckoutButtonVariables | null>(null);

  const onFormSubmit = async (values: FormValues) => {
    const receivedLiqpayCheckoutButtonVariables =
      await fundraisingService.createDonationLiqPayOrder({
        amount: Number(values.amount),
      });

    setLiqpayCheckoutButtonVariables(receivedLiqpayCheckoutButtonVariables);
  };

  const isFormDisabled = !!liqpayCheckoutButtonVariables;

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
        <Formik
          initialValues={FORM_INITIAL_VALUES}
          onSubmit={onFormSubmit}
          validationSchema={FORM_VALIDATION_SCHEMA}
        >
          {({ isValid, isSubmitting }) => {
            return (
              <Form>
                <FormikTextInputField
                  name='amount'
                  label='Сума'
                  placeholder='Сума'
                  type='number'
                  isRequired
                  min={1}
                  readOnly={isFormDisabled}
                />
                <Button
                  type='submit'
                  disabled={!isValid || isSubmitting || isFormDisabled}
                >
                  Підтримати
                </Button>
              </Form>
            );
          }}
        </Formik>
        {liqpayCheckoutButtonVariables && (
          <LiqpayCheckoutButton {...liqpayCheckoutButtonVariables} />
        )}
      </ModalContainer>
    </Modal>
  );
};

export { SupportFundraisingModal };

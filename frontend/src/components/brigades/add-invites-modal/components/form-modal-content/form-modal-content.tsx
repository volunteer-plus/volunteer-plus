import { Form, Formik } from 'formik';
import { useMemo } from 'react';

import { Button, FormikTextInputField } from '@/components/common';
import { Yup } from '@/yup';

import styles from './styles.module.scss';
import { ADD_INVITES_FORM_INITIAL_VALUES } from './constants';
import { AddInvitesFormValues } from './types';

interface Props {
  onSubmit: (values: AddInvitesFormValues) => void;
  onCancel: () => void;
}

const FormModalContent: React.FC<Props> = ({ onCancel, onSubmit }) => {
  const validationSchema = useMemo(() => {
    return Yup.object({
      invitesNumber: Yup.number()
        .required()
        .integer()
        .min(1)
        .label('Кількість запрошень'),
    });
  }, []);

  return (
    <Formik
      initialValues={ADD_INVITES_FORM_INITIAL_VALUES}
      onSubmit={onSubmit}
      validationSchema={validationSchema}
    >
      {({ isValid }) => {
        return (
          <Form>
            <FormikTextInputField
              name='invitesNumber'
              isRequired
              label='Кількість запрошень'
              placeholder='123'
              type='number'
            />
            <div className={styles.actions}>
              <Button
                type='button'
                variant='outlined'
                colorSchema='gray'
                onClick={() => onCancel()}
              >
                Скасувати
              </Button>
              <Button type='submit' disabled={!isValid}>
                Додати запрошення
              </Button>
            </div>
          </Form>
        );
      }}
    </Formik>
  );
};

export { FormModalContent };

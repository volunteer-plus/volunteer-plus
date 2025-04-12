import { Form, Formik } from 'formik';
import { useEffect } from 'react';
import {
  Modal,
  ModalContainer,
  ModalTitle,
  ModalCloseButton,
  FormikTextInputField,
  Button,
  FormikTextareaField,
  FormikSelectField,
} from '@/components/common';
import { useAppDispatch } from '@/hooks/store';
import { reloadBrigades } from '@/slices/brigades';
import { loadBrigadeCodesIfNotLoaded } from '@/slices/brigade-codes';
import { brigadesService } from '@/services/brigades/brigades';
import { useBrigadeCodeOptions } from '@/hooks/brigades';

import styles from './styles.module.scss';
import {
  ADD_BRIGADE_FORM_INITIAL_VALUES,
  ADD_BRIGADE_FORM_VALIDATION_SCHEMA,
} from './constants';

import { AddBrigadeFormValidatedValues } from './types';

type Props = Pick<
  React.ComponentPropsWithoutRef<typeof Modal>,
  'isOpen' | 'onClose'
> & {
  onAfterSubmit?: () => void;
};

const AddBrigadeModal: React.FC<Props> = ({
  isOpen,
  onClose,
  onAfterSubmit,
}) => {
  const dispatch = useAppDispatch();

  useEffect(() => {
    dispatch(loadBrigadeCodesIfNotLoaded());
  });

  const brigadeCodeOptions = useBrigadeCodeOptions();

  const handleFormSubmit = async (values: AddBrigadeFormValidatedValues) => {
    await brigadesService.createOrUpdateBrigades([
      {
        name: values.name,
        regimentCode: values.regimentCode,
        description: values.description || null,
      },
    ]);

    await dispatch(reloadBrigades());

    onAfterSubmit?.();
  };

  return (
    <Modal isOpen={isOpen} onClose={onClose}>
      <ModalContainer className={styles.container}>
        <ModalTitle>Додати бригаду</ModalTitle>
        <ModalCloseButton onClick={onClose} />
        <Formik
          initialValues={ADD_BRIGADE_FORM_INITIAL_VALUES}
          onSubmit={async (values) => {
            await handleFormSubmit(values as AddBrigadeFormValidatedValues);
          }}
          validationSchema={ADD_BRIGADE_FORM_VALIDATION_SCHEMA}
        >
          {({ isValid, isSubmitting }) => {
            return (
              <Form>
                <div className={styles.fields}>
                  <FormikTextInputField
                    name='name'
                    label='Назва бригади'
                    placeholder='3 ОШБ'
                    isRequired
                  />
                  <FormikSelectField
                    name='regimentCode'
                    label='Код бригади'
                    placeholder='Оберіть код бригади'
                    isRequired
                    options={brigadeCodeOptions}
                  />
                  <FormikTextInputField
                    name='adminEmail'
                    label='Email адміністратора бригади'
                    placeholder='example@example.ua'
                    isRequired
                  />
                  <FormikTextareaField
                    name='description'
                    label='Опис бригади'
                    placeholder='Коротко про бригаду'
                  />
                </div>
                <div className={styles.actions}>
                  <Button
                    type='button'
                    onClick={onClose}
                    variant='outlined'
                    colorSchema='gray'
                  >
                    Скасувати
                  </Button>
                  <Button type='submit' disabled={!isValid || isSubmitting}>
                    Зберегти
                  </Button>
                </div>
              </Form>
            );
          }}
        </Formik>
      </ModalContainer>
    </Modal>
  );
};

export { AddBrigadeModal };

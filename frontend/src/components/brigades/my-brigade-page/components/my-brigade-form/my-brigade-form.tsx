import { Form, Formik } from 'formik';
import { useMemo } from 'react';

import {
  Button,
  FormikSelectField,
  FormikTextareaField,
  FormikTextInputField,
} from '@/components/common';
import { useAppDispatch, useAppSelector } from '@/hooks/store';
import { useBrigadeCodeOptions } from '@/hooks/brigades';
import { reloadMyBrigade } from '@/slices/my-brigade';
import { brigadesService } from '@/services/brigades/brigades';

import styles from './styles.module.scss';
import { MyBrigadeFormValidatedValues, MyBrigadeFormValues } from './types';
import { MY_BRIGADE_FORM_VALIDATION_SCHEMA } from './constants';

const MyBrigadeForm: React.FC = () => {
  const dispatch = useAppDispatch();

  const myBrigadeState = useAppSelector((state) => state.myBrigade);

  const brigadeCodeOptions = useBrigadeCodeOptions();

  const initialValues = useMemo<MyBrigadeFormValues>(() => {
    if (myBrigadeState.data) {
      return {
        adminEmail: 'a@a.com',
        description: myBrigadeState.data.value.description,
        name: myBrigadeState.data.value.name,
        regimentCode: myBrigadeState.data.value.regimentCode,
      };
    }

    return {
      adminEmail: '',
      description: '',
      name: '',
      regimentCode: null,
    };
  }, [myBrigadeState.data]);

  const handleFormSubmit = async (values: MyBrigadeFormValidatedValues) => {
    await brigadesService.createOrUpdateBrigades([
      {
        name: values.name,
        regimentCode: values.regimentCode,
        description: values.description || null,
      },
    ]);

    await dispatch(reloadMyBrigade());
  };

  const areFieldsDisabled = !myBrigadeState.data;

  return (
    <Formik
      initialValues={initialValues}
      onSubmit={async (values) => {
        await handleFormSubmit(values as MyBrigadeFormValidatedValues);
      }}
      enableReinitialize
      validationSchema={MY_BRIGADE_FORM_VALIDATION_SCHEMA}
    >
      {({ dirty, isValid, isSubmitting }) => {
        return (
          <Form className={styles.form}>
            <div className={styles.fieldsContainer}>
              <FormikTextInputField
                label='Назва бригади'
                isRequired
                placeholder='3 ОШБ'
                name='name'
                disabled={areFieldsDisabled}
              />
              <FormikSelectField
                name='regimentCode'
                label='Код бригади'
                placeholder='Оберіть код бригади'
                isRequired
                options={brigadeCodeOptions}
                isDisabled
              />
              <FormikTextInputField
                name='adminEmail'
                label='Email адміністратора бригади'
                placeholder='example@example.ua'
                isRequired
                disabled
              />
              <FormikTextareaField
                name='description'
                label='Опис бригади'
                placeholder='Коротко про бригаду'
                disabled={areFieldsDisabled}
              />
            </div>
            <Button
              type='submit'
              disabled={!dirty || !isValid || areFieldsDisabled || isSubmitting}
            >
              Зберегти
            </Button>
          </Form>
        );
      }}
    </Formik>
  );
};

export { MyBrigadeForm };

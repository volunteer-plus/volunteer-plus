import { Form, Formik } from 'formik';

import {
  Button,
  FormikTextareaField,
  FormikTextInputField,
} from '@/components/common';

import styles from './styles.module.scss';

const MyBrigadeForm: React.FC = () => {
  return (
    <Formik initialValues={{}} onSubmit={() => {}}>
      <Form className={styles.form}>
        <div className={styles.fieldsContainer}>
          <FormikTextInputField
            label='Назва бригади'
            isRequired
            placeholder='3 ОЩБ'
            name='name'
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
          />
        </div>
        <Button type='submit'>Зберегти</Button>
      </Form>
    </Formik>
  );
};

export { MyBrigadeForm };

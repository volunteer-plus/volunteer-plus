import classNames from 'classnames';

import styles from './styles.module.scss';
import { Form } from 'react-router-dom';
import { Formik } from 'formik';
import {
  Button,
  FormikSelectField,
  FormikTextareaField,
  FormikTextInputField,
} from '@/components/common';
import { FormikDeliveryDateField } from '@/components/requests';

type Props = React.ComponentPropsWithoutRef<typeof Form>;

const RequestForm: React.FC<Props> = ({ className, ...props }) => {
  return (
    <Formik initialValues={{}} onSubmit={async () => null}>
      <Form {...props} className={classNames(styles.root, className)}>
        <div className={styles.fields}>
          <FormikTextInputField
            name='title'
            label='Заголовок'
            isRequired
            placeholder='Коротко, що необхідно'
          />
          <FormikSelectField
            name='category'
            label='Категорія'
            isRequired
            placeholder='Оберіть категорію'
            options={[
              {
                value: 'медикаменти',
                label: 'Медикаменти',
              },
              {
                value: 'амуніція',
                label: 'Амуніція',
              },
              {
                value: 'дрони',
                label: 'Дрони',
              },
              {
                value: 'набої',
                label: 'Набої',
              },
            ]}
          />
          <FormikTextareaField
            name='description'
            label='Опис'
            placeholder='Деталі запиту'
          />
          <FormikDeliveryDateField
            name='deliveryDate'
            label='Крайній термін поставки'
            isRequired
          />
        </div>
        <div className={styles.actions}>
          <Button type='submit'>Зберегти</Button>
        </div>
      </Form>
    </Formik>
  );
};

export { RequestForm };

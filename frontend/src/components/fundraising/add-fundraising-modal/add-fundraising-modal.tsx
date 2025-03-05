import { Form, Formik } from 'formik';
import {
  Button,
  FormikSelectField,
  FormikTextareaField,
  FormikTextInputField,
  Modal,
  ModalCloseButton,
  ModalContainer,
  ModalTitle,
} from '@/components/common';

import { FORM_VALIDATION_SCHEMA } from './constants';
import styles from './styles.module.scss';

type Props = Pick<React.ComponentProps<typeof Modal>, 'onClose' | 'isOpen'>;

const AddFundraisingModal: React.FC<Props> = ({ isOpen, onClose }) => {
  return (
    <Modal isOpen={isOpen} onClose={onClose}>
      <ModalContainer>
        <ModalTitle>Додати збір</ModalTitle>
        <ModalCloseButton onClick={() => onClose()} />
        <Formik
          initialValues={{}}
          onSubmit={async () => null}
          validationSchema={FORM_VALIDATION_SCHEMA}
        >
          <Form>
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
              <FormikTextInputField
                name='amount'
                label='Сума'
                isRequired
                placeholder='Сума збору'
                units='₴'
                type='number'
              />
              <FormikTextareaField
                name='description'
                label='Опис'
                placeholder='Деталі запиту'
              />
            </div>

            <div className={styles.actions}>
              <Button
                variant='outlined'
                colorSchema='gray'
                type='button'
                onClick={() => onClose()}
              >
                Скасувати
              </Button>
              <Button type='submit'>Зберегти</Button>
            </div>
          </Form>
        </Formik>
      </ModalContainer>
    </Modal>
  );
};

export { AddFundraisingModal };

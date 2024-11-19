import { Form, Formik } from 'formik';
import {
  Modal,
  ModalContainer,
  ModalTitle,
  ModalCloseButton,
  FormikTextInputField,
  Button,
  FormikTextareaField,
} from '@/components/common';
import styles from './styles.module.scss';

type Props = Pick<
  React.ComponentPropsWithoutRef<typeof Modal>,
  'isOpen' | 'onClose'
>;

const AddBrigadeModal: React.FC<Props> = ({ isOpen, onClose }) => {
  return (
    <Modal isOpen={isOpen} onClose={onClose}>
      <ModalContainer className={styles.container}>
        <ModalTitle>Додати бригаду</ModalTitle>
        <ModalCloseButton onClick={onClose} />
        <Formik initialValues={{}} onSubmit={async () => null}>
          <Form>
            <div className={styles.fields}>
              <FormikTextInputField
                name='name'
                label='Назва бригади'
                placeholder='3 ОШБ'
                isRequired
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
              <Button type='submit'>Зберегти</Button>
            </div>
          </Form>
        </Formik>
      </ModalContainer>
    </Modal>
  );
};

export { AddBrigadeModal };

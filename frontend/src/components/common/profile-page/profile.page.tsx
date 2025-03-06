import { AdminPageContent } from '@/components/admin';
import { Authenticated } from '@/components/auth';
import {
  PageTitle,
  Button,
  Avatar,
  FormikTextInputField,
  MaterialSymbol,
} from '@/components/common';

import styles from './styles.module.scss';
import { Form, Formik } from 'formik';

const BareProfilePage: React.FC = () => {
  return (
    <AdminPageContent className={styles.root}>
      <div className={styles.content}>
        <div>
          <PageTitle className={styles.pageTitle}>Профіль</PageTitle>
          <Formik initialValues={{}} onSubmit={() => {}}>
            <Form className={styles.form}>
              <div className={styles.fields}>
                <FormikTextInputField name='email' label='Email' isRequired />
                <FormikTextInputField
                  name='lastName'
                  label='Прізвище'
                  isRequired
                />
                <FormikTextInputField
                  name='firstName'
                  label='Ім’я'
                  isRequired
                />
                <FormikTextInputField name='middleName' label='По-батькові' />
                <FormikTextInputField
                  name='dateOfBirth'
                  label='Дата народження'
                  type='date'
                />
              </div>
              <Button className={styles.saveButton} type='submit'>
                Зберегти
              </Button>
            </Form>
          </Formik>
        </div>
        <div className={styles.avatarWrapper}>
          <Avatar
            size='160px'
            imageSrc='https://i.pinimg.com/474x/98/51/1e/98511ee98a1930b8938e42caf0904d2d.jpg'
          />
          <button className={styles.editAvatarButton}>
            <MaterialSymbol className={styles.editAvatarIcon}>
              edit
            </MaterialSymbol>
          </button>
        </div>
      </div>
    </AdminPageContent>
  );
};

const ProfilePage = Authenticated(BareProfilePage);

export { ProfilePage };

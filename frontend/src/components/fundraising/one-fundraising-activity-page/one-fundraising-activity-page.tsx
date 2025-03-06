import { Link, useParams } from 'react-router-dom';
import { Form, Formik } from 'formik';
import { useState } from 'react';

import { AdminPageContent } from '@/components/admin';
import {
  Button,
  ConfirmationModal,
  FieldLabel,
  FormikSelectField,
  FormikTextareaField,
  FormikTextInputField,
  PageBackButton,
  PageTitle,
  ProgressTag,
} from '@/components/common';
import { Authenticated } from '@/components/auth';
import {
  FundraisingDonations,
  FundraisingProgressBar,
} from '@/components/fundraising';

import styles from './styles.module.scss';
import { FundraisingRequest } from './components';

const BareOneFundraisingActivityPage: React.FC = () => {
  const { id } = useParams();

  const [isCloseModalOpen, setIsCloseModalOpen] = useState(false);

  return (
    <AdminPageContent className={styles.pageContent}>
      <PageBackButton as={Link} to='/fundraising-activities'>
        Назад до усіх зборів
      </PageBackButton>
      <PageTitle className={styles.pageTitle}>Збір №{id}</PageTitle>
      <div className={styles.sides}>
        <Formik initialValues={{}} onSubmit={async () => null}>
          <Form className={styles.leftSide}>
            <div className={styles.fields}>
              <div className={styles.statusAndButton}>
                <div>
                  <FieldLabel bottomMargin>Статус:</FieldLabel>
                  <ProgressTag progress={0.75} size='medium'>
                    Іде збір
                  </ProgressTag>
                </div>
                <Button>Завантажити звіт</Button>
              </div>
              <div>
                <FieldLabel bottomMargin>Запит:</FieldLabel>
                <FundraisingRequest
                  requestId={123}
                  brigadeName='3 ОШБ'
                  title='Бронежелети'
                />
              </div>
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
            <div className={styles.formActions}>
              <Button
                variant='outlined'
                colorSchema='olive'
                type='button'
                onClick={() => setIsCloseModalOpen(true)}
              >
                Закрити збір
              </Button>
              <Button type='submit'>Зберегти</Button>
            </div>
          </Form>
        </Formik>
        <div className={styles.rightSide}>
          <h2 className={styles.donationsTitle}>Пожертви</h2>
          <FundraisingProgressBar
            raised={1000}
            target={5000}
            className={styles.donationsProgressBar}
          />
          <FundraisingDonations className={styles.donations} />
        </div>
      </div>
      <ConfirmationModal
        isOpen={isCloseModalOpen}
        onCancel={() => setIsCloseModalOpen(false)}
        title='Закрити збір'
        cancelText='Скасувати'
        confirmText='Так, закрити'
      >
        Ви впевнені, що хочете закрити збір?
      </ConfirmationModal>
    </AdminPageContent>
  );
};

const OneFundraisingActivityPage = Authenticated(
  BareOneFundraisingActivityPage,
  {
    allowedRoles: {
      volunteer: true,
    },
  }
);

export { OneFundraisingActivityPage };

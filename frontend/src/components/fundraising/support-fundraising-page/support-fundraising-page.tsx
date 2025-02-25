import { Formik } from 'formik';
import { useMemo } from 'react';
import { NarrowPageContent, PageTitle, Tag } from '@/components/common';
import {
  DonationsStatistics,
  FundraisingProgressBar,
} from '@/components/fundraising';

import styles from './styles.module.scss';
import { SupportForm } from './components';
import { Yup } from '@/yup';

const SupportFundraisingPage: React.FC = () => {
  const formInitialValues = useMemo(() => {
    return {
      email: '',
      amount: '',
    };
  }, []);

  const formValidationSchema = useMemo(() => {
    return Yup.object({
      email: Yup.string().email().required().label('Email'),
      amount: Yup.number().required().positive().label('Сума внеску'),
    });
  }, []);

  return (
    <>
      <NarrowPageContent>
        <PageTitle className={styles.pageTitle}>Підтримати збір</PageTitle>
        <h2 className={styles.activityName}>
          На FPV 100 дронів для нищення окупантів
        </h2>
        <div className={styles.categoryAndBrigadeLine}>
          <Tag size='small'>Дрони</Tag>
          <div className={styles.brigadeLabel}>
            Для <span className={styles.brigadeName}>3 ОШБ</span>
          </div>
        </div>
        <div className={styles.volunteer}>
          Збирає{' '}
          <span className={styles.volunteerName}>Васильченеко В. І.</span>
        </div>
        <FundraisingProgressBar
          target={100000}
          raised={50000}
          className={styles.progressBar}
        />
        <div className={styles.activityDescription}>
          Lorem ipsum dolor, sit amet consectetur adipisicing elit. Quisquam,
          ullam quod, vel illum nam voluptas dignissimos harum repellat nesciunt
          possimus praesentium, qui quam voluptates sit officia similique ipsam
          quis natus.
        </div>
        <Formik
          initialValues={formInitialValues}
          onSubmit={async () => null}
          validationSchema={formValidationSchema}
        >
          <SupportForm className={styles.form} />
        </Formik>
      </NarrowPageContent>
      <DonationsStatistics />
    </>
  );
};

export { SupportFundraisingPage };

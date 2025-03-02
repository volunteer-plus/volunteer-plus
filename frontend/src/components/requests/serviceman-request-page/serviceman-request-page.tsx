import classNames from 'classnames';
import { Link, useParams } from 'react-router-dom';

import { AdminPageContent } from '@/components/admin';
import { PageBackButton, PageTitle } from '@/components/common';

import { OffersContainer, RequestForm } from './components';
import styles from './styles.module.scss';
import { Authenticated } from '@/components/auth';

const BareServicemanRequestPage: React.FC = () => {
  const { id: requestId } = useParams();

  return (
    <AdminPageContent className={styles.content}>
      <PageBackButton as={Link} to='/serviceman/requests'>
        Назад до усіх запитів
      </PageBackButton>
      <PageTitle>Запит №{requestId}</PageTitle>
      <div className={styles.formAndOffers}>
        <RequestForm />
        <OffersContainer />
      </div>
    </AdminPageContent>
  );
};

const ServicemanRequestPage = Authenticated(BareServicemanRequestPage, {
  allowedRoles: {
    serviceman: true,
  },
});

export { ServicemanRequestPage };

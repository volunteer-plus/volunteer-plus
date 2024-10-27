import React from 'react';

import {
  Button,
  FundraisingItem,
  FundraisingList,
  PageContent,
  PageFooter,
  PageHeader,
  PageLayout,
} from '@/components';

import styles from './styles.module.scss';

const HomePage: React.FC = () => {
  return (
    <PageLayout>
      <PageHeader />
      <PageContent>
        <div className={styles.fundraisingListTitle}>
          <h2 className={styles.fundraisingListTitleText}>Активні збори</h2>
          <Button>Більше зборів</Button>
        </div>
        <FundraisingList>
          <FundraisingItem />
          <FundraisingItem />
          <FundraisingItem />
          <FundraisingItem />
          <FundraisingItem />
          <FundraisingItem />
          <FundraisingItem />
          <FundraisingItem />
          <FundraisingItem />
        </FundraisingList>
      </PageContent>
      <PageFooter />
    </PageLayout>
  );
};

export { HomePage };

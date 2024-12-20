import React from 'react';

import { Button, PageContent } from '@/components/common';
import { FundraisingItem, FundraisingList } from '@/components/fundraising';

import styles from './styles.module.scss';

const HomePage: React.FC = () => {
  return (
    <PageContent>
      <div className={styles.fundraisingListTitle}>
        <h2 className={styles.fundraisingListTitleText}>Активні збори</h2>
        <Button colorSchema='gray' variant='outlined'>
          Більше зборів
        </Button>
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
  );
};

export { HomePage };

import { Button } from '@/components';

import Logo from '@/assets/logo.svg?react';

import styles from './styles.module.scss';

const PageHeader: React.FC = () => {
  return (
    <header className={styles.header}>
      <Logo />
      <div className={styles.actions}>
        <Button>Увійти</Button>
        <Button>Створити профіль</Button>
      </div>
    </header>
  );
};

export { PageHeader };

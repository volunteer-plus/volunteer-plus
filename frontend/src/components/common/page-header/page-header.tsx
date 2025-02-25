import { Link } from 'react-router-dom';
import { ButtonBase } from '@/components/common';

import Logo from '@/assets/logo.svg?react';

import styles from './styles.module.scss';

const PageHeader: React.FC = () => {
  return (
    <header className={styles.header}>
      <Link to='/'>
        <Logo />
      </Link>
      <div className={styles.actions}>
        <ButtonBase
          colorSchema='gray'
          variant='outlined'
          elementType={Link}
          elementProps={{ to: '/sign-in', children: 'Увійти' }}
        />
        <ButtonBase
          elementType={Link}
          elementProps={{ to: '/sign-up', children: 'Створити профіль' }}
        />
      </div>
    </header>
  );
};

export { PageHeader };

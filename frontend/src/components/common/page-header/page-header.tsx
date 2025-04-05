import { Link } from 'react-router-dom';
import { ButtonBase, SessionUserBlock } from '@/components/common';

import Logo from '@/assets/logo.svg?react';

import styles from './styles.module.scss';
import { useAppSelector } from '@/hooks/store';

const PageHeader: React.FC = () => {
  const userState = useAppSelector((state) => state.user);

  return (
    <header className={styles.header}>
      <Link to='/'>
        <Logo />
      </Link>

      {userState.user ? (
        <SessionUserBlock />
      ) : (
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
      )}
    </header>
  );
};

export { PageHeader };

import { Link, useNavigate } from 'react-router-dom';
import {
  ButtonBase,
  MenuItem,
  MenuItemIconMaterial,
  SessionUserBlock,
} from '@/components/common';

import Logo from '@/assets/logo.svg?react';

import styles from './styles.module.scss';
import { useAppSelector } from '@/hooks/store';
import { getUserHomepage } from '@/helpers/user';

const PageHeader: React.FC = () => {
  const userState = useAppSelector((state) => state.user);
  const navigate = useNavigate();

  return (
    <header className={styles.header}>
      <Link to='/'>
        <Logo />
      </Link>

      {userState.user ? (
        <SessionUserBlock
          menuItems={
            <>
              <MenuItem
                onClick={() => {
                  if (!userState.user) return;

                  navigate(getUserHomepage(userState.user));
                }}
                leftIcon={
                  <MenuItemIconMaterial>arrow_back</MenuItemIconMaterial>
                }
              >
                Назад
              </MenuItem>
            </>
          }
        />
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

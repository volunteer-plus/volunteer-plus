import { ButtonBase } from '@/components/common';

import Logo from '@/assets/logo.svg?react';

import styles from './styles.module.scss';
import { Link } from 'react-router-dom';

const AdminPageHeader: React.FC = () => {
  return (
    <header className={styles.header}>
      <div className={styles.leftPart}>
        <div className={styles.burgerWrapper}>
          <button type='button' className={styles.burgerButton}>
            <span className='material-symbols-outlined'>menu</span>
          </button>
        </div>
        <Logo className={styles.logo} />
      </div>
      <div className={styles.rightPart}></div>
    </header>
  );
};

export { AdminPageHeader };
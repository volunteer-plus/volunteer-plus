import Logo from '@/assets/logo.svg?react';

import styles from './styles.module.scss';

const PageFooter: React.FC = () => {
  return (
    <footer className={styles.footer}>
      <div className={styles.content}>
        <Logo className={styles.logo} />
      </div>
      <div className={styles.footerOfFooter}>
        © 2024 Волонтер+. Всі права захищені
      </div>
    </footer>
  );
};

export { PageFooter };

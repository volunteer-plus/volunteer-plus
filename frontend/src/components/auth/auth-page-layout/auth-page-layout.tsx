import Logo from '@/assets/logo.svg?react';
import styles from './styles.module.scss';

interface Props {
  children: React.ReactNode;
}

const AuthPageLayout: React.FC<Props> = ({ children }) => {
  return (
    <div className={styles.root}>
      <main className={styles.main}>
        <div className={styles.logoWrapper}>
          <Logo className={styles.logo} />
        </div>
        <div className={styles.content}>{children}</div>
      </main>
    </div>
  );
};

export { AuthPageLayout };

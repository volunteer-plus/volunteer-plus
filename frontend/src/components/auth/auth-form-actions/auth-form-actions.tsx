import styles from './styles.module.scss';

const AuthFormActions: React.FC<React.ComponentPropsWithoutRef<'div'>> = (
  props
) => {
  return <div {...props} className={styles.actions} />;
};

export { AuthFormActions };

import styles from './styles.module.scss';

type Props = React.ComponentPropsWithoutRef<'label'> & {
  isRequired?: boolean;
};

const FieldLabel: React.FC<Props> = ({ children, isRequired, ...props }) => {
  return (
    <label {...props} className={styles.label}>
      {children}
      {isRequired && <span className={styles.asterisk}> *</span>}
    </label>
  );
};

export { FieldLabel };

import classNames from 'classnames';
import styles from './styles.module.scss';

type Props = React.ComponentPropsWithoutRef<'label'> & {
  isRequired?: boolean;
};

const FieldLabel: React.FC<Props> = ({
  children,
  isRequired,
  className,
  ...props
}) => {
  return (
    <label {...props} className={classNames(className, styles.label)}>
      {children}
      {isRequired && <span className={styles.asterisk}> *</span>}
    </label>
  );
};

export { FieldLabel };

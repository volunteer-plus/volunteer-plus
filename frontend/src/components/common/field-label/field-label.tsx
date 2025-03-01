import classNames from 'classnames';
import styles from './styles.module.scss';

type Props = React.ComponentPropsWithoutRef<'label'> & {
  isRequired?: boolean;
  bottomMargin?: boolean;
};

const FieldLabel: React.FC<Props> = ({
  children,
  isRequired,
  className,
  bottomMargin = false,
  ...props
}) => {
  return (
    <label
      {...props}
      className={classNames(className, styles.label, {
        [styles.bottomMargin]: bottomMargin,
      })}
    >
      {children}
      {isRequired && <span className={styles.asterisk}> *</span>}
    </label>
  );
};

export { FieldLabel };

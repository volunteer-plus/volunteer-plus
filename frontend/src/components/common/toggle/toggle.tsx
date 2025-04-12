import classNames from 'classnames';
import styles from './styles.module.scss';

type Props = Omit<React.ComponentPropsWithoutRef<'input'>, 'type'>;

const Toggle: React.FC<Props> = ({ className, ...props }) => {
  return (
    <label className={classNames(styles.label, className)}>
      <input {...props} type='checkbox' className={styles.input} />
      <div className={styles.toggle}>
        <div className={styles.lever} />
      </div>
    </label>
  );
};

export { Toggle };

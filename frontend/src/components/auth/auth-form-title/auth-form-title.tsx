import classNames from 'classnames';
import styles from './styles.module.scss';

const AuthFormTitle: React.FC<React.ComponentPropsWithoutRef<'h2'>> = (
  props
) => {
  return (
    <h2 {...props} className={classNames(styles.title, props.className)} />
  );
};

export { AuthFormTitle };

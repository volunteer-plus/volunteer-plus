import classNames from 'classnames';

import styles from './styles.module.scss';

const AdminPageTitle: React.FC<React.ComponentPropsWithoutRef<'h1'>> = ({
  className,
  ...props
}) => {
  return <h1 {...props} className={classNames(styles.title, className)} />;
};

export { AdminPageTitle };
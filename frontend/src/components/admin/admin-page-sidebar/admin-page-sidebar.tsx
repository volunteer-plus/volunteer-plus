import classNames from 'classnames';

import styles from './styles.module.scss';

const AdminPageSidebar: React.FC<React.ComponentPropsWithoutRef<'div'>> = ({
  className,
  ...props
}) => {
  return (
    <aside {...props} className={classNames(styles.sidebar, className)}></aside>
  );
};

export { AdminPageSidebar };

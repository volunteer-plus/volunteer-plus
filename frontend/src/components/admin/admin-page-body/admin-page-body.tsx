import classNames from 'classnames';

import styles from './styles.module.scss';

const AdminPageBody: React.FC<React.ComponentPropsWithoutRef<'div'>> = ({
  className,
  ...props
}) => {
  return <div {...props} className={classNames(styles.body, className)} />;
};

export { AdminPageBody };

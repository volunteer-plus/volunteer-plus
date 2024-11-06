import classNames from 'classnames';

import styles from './styles.module.scss';

const AdminPageLayout: React.FC<React.ComponentPropsWithoutRef<'div'>> = ({
  className,
  ...props
}) => {
  return <div {...props} className={classNames(styles.layout, className)} />;
};

export { AdminPageLayout };

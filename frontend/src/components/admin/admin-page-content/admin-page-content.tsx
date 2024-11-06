import classNames from 'classnames';

import styles from './styles.module.scss';

const AdminPageContent: React.FC<React.ComponentPropsWithoutRef<'div'>> = ({
  className,
  ...props
}) => {
  return <main {...props} className={classNames(styles.content, className)} />;
};

export { AdminPageContent };

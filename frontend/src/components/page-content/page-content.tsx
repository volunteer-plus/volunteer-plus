import classNames from 'classnames';

import styles from './styles.module.scss';

const PageContent: React.FC<React.ComponentPropsWithoutRef<'div'>> = ({
  className,
  ...props
}) => {
  return <main {...props} className={classNames(styles.content, className)} />;
};

export { PageContent };

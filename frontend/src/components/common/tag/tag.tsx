import classNames from 'classnames';

import styles from './styles.module.scss';

const Tag: React.FC<React.ComponentPropsWithoutRef<'span'>> = ({
  className,
  ...props
}) => {
  return <span {...props} className={classNames(styles.tag, className)} />;
};

export { Tag };

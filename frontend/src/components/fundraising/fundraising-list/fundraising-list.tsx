import classNames from 'classnames';

import styles from './styles.module.scss';

const FundraisingList: React.FC<React.ComponentPropsWithoutRef<'div'>> = ({
  className,
  ...props
}) => {
  return <div {...props} className={classNames(styles.list, className)} />;
};

export { FundraisingList };

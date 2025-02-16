import classNames from 'classnames';

import styles from './styles.module.scss';

const TableHead: React.FC<React.ComponentProps<'thead'>> = ({
  className,
  ...props
}) => {
  return <thead {...props} className={classNames(className, styles.head)} />;
};

export { TableHead };

import classNames from 'classnames';

import styles from './styles.module.scss';

const Table: React.FC<React.ComponentProps<'table'>> = ({
  className,
  ...props
}) => {
  return <table {...props} className={classNames(className, styles.table)} />;
};

export { Table };

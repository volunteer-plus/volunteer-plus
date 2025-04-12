import classNames from 'classnames';

import styles from './styles.module.scss';

const TableRow: React.FC<React.ComponentProps<'tr'>> = ({
  className,
  ...props
}) => {
  return <tr {...props} className={classNames(className, styles.row)} />;
};

export { TableRow };

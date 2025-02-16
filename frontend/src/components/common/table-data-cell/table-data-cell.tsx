import classNames from 'classnames';

import styles from './styles.module.scss';

const TableDataCell: React.FC<React.ComponentProps<'td'>> = ({
  className,
  ...props
}) => {
  return <td {...props} className={classNames(className, styles.cell)} />;
};

export { TableDataCell };

import classNames from 'classnames';

import styles from './styles.module.scss';

const TableHeaderCell: React.FC<React.ComponentProps<'th'>> = ({
  className,
  ...props
}) => {
  return <th {...props} className={classNames(className, styles.cell)} />;
};

export { TableHeaderCell };

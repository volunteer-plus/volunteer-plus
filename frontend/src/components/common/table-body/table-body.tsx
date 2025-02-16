import classNames from 'classnames';

import styles from './styles.module.scss';

const TableBody: React.FC<React.ComponentProps<'tbody'>> = ({
  className,
  ...props
}) => {
  return <tbody {...props} className={classNames(className, styles.body)} />;
};

export { TableBody };

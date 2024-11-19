import classNames from 'classnames';

import styles from './styles.module.scss';

const ModalTitle: React.FC<React.ComponentPropsWithoutRef<'h2'>> = ({
  className,
  ...props
}) => {
  return <h2 {...props} className={classNames(styles.title, className)} />;
};

export { ModalTitle };

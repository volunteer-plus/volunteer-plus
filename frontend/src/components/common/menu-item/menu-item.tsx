import classNames from 'classnames';
import styles from './styles.module.scss';

interface Props {
  children: React.ReactNode;
  leftIcon?: React.ReactNode;
  isActive?: boolean;
}

const MenuItem: React.FC<Props> = ({
  children,
  leftIcon,
  isActive = false,
}) => {
  return (
    <button
      className={classNames(styles.item, {
        [styles.active]: isActive,
      })}
    >
      {leftIcon}
      <div>{children}</div>
    </button>
  );
};

export { MenuItem };

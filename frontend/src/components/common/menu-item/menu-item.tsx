import classNames from 'classnames';
import styles from './styles.module.scss';

interface Props extends React.ButtonHTMLAttributes<HTMLButtonElement> {
  children: React.ReactNode;
  leftIcon?: React.ReactNode;
  isActive?: boolean;
}

const MenuItem: React.FC<Props> = ({
  children,
  leftIcon,
  isActive = false,
  className,
  ...props
}) => {
  return (
    <button
      {...props}
      className={classNames(className, styles.item, {
        [styles.active]: isActive,
      })}
    >
      {leftIcon}
      <div>{children}</div>
    </button>
  );
};

export { MenuItem };

import classNames from 'classnames';
import styles from './styles.module.scss';

interface Props {
  children: string;
}

const MenuItemIconMaterial: React.FC<Props> = ({ children }) => {
  return (
    <span className={classNames('material-symbols-outlined', styles.icon)}>
      {children}
    </span>
  );
};

export { MenuItemIconMaterial };

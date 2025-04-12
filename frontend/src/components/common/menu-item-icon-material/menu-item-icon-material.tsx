import { MaterialSymbol } from '@/components/common';

import styles from './styles.module.scss';

interface Props {
  children: string;
}

const MenuItemIconMaterial: React.FC<Props> = ({ children }) => {
  return <MaterialSymbol className={styles.icon}>{children}</MaterialSymbol>;
};

export { MenuItemIconMaterial };

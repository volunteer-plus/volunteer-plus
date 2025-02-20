import { useCallback, useRef, useState } from 'react';
import {
  Button,
  ButtonMaterialSymbol,
  Menu,
  MenuContainer,
} from '@/components/common';
import { useClickOutside } from '@/hooks/common';

import styles from './styles.module.scss';

interface Props extends Omit<React.ComponentProps<typeof Button>, 'children'> {
  children: React.ReactNode;
}

const FilterButton: React.FC<Props> = ({ children }) => {
  const buttonRef = useRef<HTMLButtonElement>(null);

  const [isMenuOpen, setIsMenuOpen] = useState(false);

  const { handlers: clickOutsideHandlers } = useClickOutside({
    callback: () => {
      setIsMenuOpen(false);
    },
    isEnabled: isMenuOpen,
  });

  const onButtonClick = useCallback<React.MouseEventHandler>(() => {
    setIsMenuOpen((prev: boolean) => !prev);
  }, []);

  return (
    <>
      <Button
        variant='filled'
        colorSchema='gray'
        ref={buttonRef}
        onClick={onButtonClick}
        leftIcon={<ButtonMaterialSymbol>filter_alt</ButtonMaterialSymbol>}
        {...clickOutsideHandlers}
      >
        Фільтр
      </Button>
      <Menu targetRef={buttonRef} isOpen={isMenuOpen} alignment='start'>
        <MenuContainer
          {...clickOutsideHandlers}
          className={styles.menuContainer}
        >
          <div className={styles.menuHeader}>Фільтр</div>
          <div className={styles.fields}>{children}</div>
        </MenuContainer>
      </Menu>
    </>
  );
};

export { FilterButton };

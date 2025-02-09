import { useRef, useState } from 'react';
import classNames from 'classnames';
import {
  Avatar,
  ExpandIcon,
  Menu,
  MenuContainer,
  MenuItem,
  MenuItemIconMaterial,
} from '@/components/common';

import styles from './styles.module.scss';
import { useClickOutside } from '@/hooks/common';
import { useLogout } from '@/hooks/auth';

type Props = React.ComponentPropsWithoutRef<'button'>;

const SessionUserBlock: React.FC<Props> = ({ className, ...props }) => {
  const logout = useLogout();

  const [isMenuOpen, setIsMenuOpen] = useState(false);

  const blockRef = useRef<HTMLButtonElement>(null);

  const clickOutsideHandlers = useClickOutside(
    () => setIsMenuOpen(false),
    isMenuOpen
  );

  return (
    <>
      <button
        {...props}
        {...clickOutsideHandlers}
        className={classNames(styles.userBlock, className)}
        type='button'
        onClick={() => setIsMenuOpen(!isMenuOpen)}
        ref={blockRef}
      >
        <ExpandIcon isExpanded={isMenuOpen} />
        <div>Васильченко В. І.</div>
        <Avatar size='30px' />
      </button>
      <Menu
        isOpen={isMenuOpen}
        targetRef={blockRef}
        side='bottom'
        alignment='stretch'
      >
        <MenuContainer {...clickOutsideHandlers}>
          <MenuItem
            leftIcon={<MenuItemIconMaterial>logout</MenuItemIconMaterial>}
            onClick={() => logout()}
          >
            Вийти
          </MenuItem>
        </MenuContainer>
      </Menu>
    </>
  );
};

export { SessionUserBlock };

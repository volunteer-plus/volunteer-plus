import { useRef, useState } from 'react';
import classNames from 'classnames';
import { Avatar, ExpandIcon, Menu, MenuContainer } from '@/components/common';

import styles from './styles.module.scss';

type Props = React.ComponentPropsWithoutRef<'button'>;

const SessionUserBlock: React.FC<Props> = ({ className, ...props }) => {
  const [isMenuOpen, setIsMenuOpen] = useState(false);

  const blockRef = useRef<HTMLButtonElement>(null);

  return (
    <button
      {...props}
      className={classNames(styles.userBlock, className)}
      type='button'
      onClick={() => setIsMenuOpen(!isMenuOpen)}
      ref={blockRef}
    >
      <ExpandIcon isExpanded={isMenuOpen} />
      <div>Васильченко В. І.</div>
      <Avatar size='30px' />
      <Menu
        isOpen={isMenuOpen}
        targetRef={blockRef}
        side='bottom'
        alignment='stretch'
      >
        <MenuContainer>Вихід</MenuContainer>
      </Menu>
    </button>
  );
};

export { SessionUserBlock };

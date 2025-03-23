import classNames from 'classnames';
import { useEffect, useState } from 'react';
import { animated } from '@react-spring/web';

import { Menu } from '@/components/common';

import styles from './styles.module.scss';

type Props = Omit<
  React.ComponentPropsWithoutRef<typeof Menu>,
  'children' | 'isOpen' | 'alignment'
> & {
  className?: string;
  children: React.ReactNode;
};

const Tooltip: React.FC<Props> = ({
  className,
  children,
  targetRef,
  side = 'top',
  ...props
}) => {
  const [isOpen, setIsOpen] = useState(false);

  useEffect(() => {
    const element = targetRef.current;

    if (!element) {
      return;
    }

    const handleMouseEnter = () => {
      setIsOpen(true);
    };

    const handleMouseLeave = () => {
      setIsOpen(false);
    };

    element.addEventListener('mouseenter', handleMouseEnter);
    element.addEventListener('mouseleave', handleMouseLeave);

    return () => {
      element.removeEventListener('mouseenter', handleMouseEnter);
      element.removeEventListener('mouseleave', handleMouseLeave);
    };
  }, [targetRef]);

  return (
    <Menu
      {...props}
      side={side}
      targetRef={targetRef}
      isOpen={isOpen}
      alignment='center'
    >
      <animated.div className={classNames(styles.tooltip, className)}>
        {children}
      </animated.div>
    </Menu>
  );
};

export { Tooltip };

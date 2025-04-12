import { animated, useSpring } from '@react-spring/web';
import { createPortal } from 'react-dom';

import styles from './styles.module.scss';
import React, { useCallback, useEffect, useRef, useState } from 'react';
import { useClickOutside, useEventCallback } from '@/hooks/common';

interface Props {
  isOpen: boolean;
  onClose: () => void;
  children: React.ReactElement;
  onOutAnimationEnd?: () => void;
}

const Modal: React.FC<Props> = ({
  isOpen,
  onClose,
  children,
  onOutAnimationEnd,
}) => {
  const [shouldRenderRoot, setShouldRenderRoot] = useState(isOpen);

  const activeExitTransitionsCountRef = useRef(0);

  useEffect(() => {
    if (isOpen) {
      setShouldRenderRoot(true);
    }
  }, [isOpen]);

  const onSpringRest = useEventCallback(() => {
    activeExitTransitionsCountRef.current -= 1;

    if (activeExitTransitionsCountRef.current === 0 && !isOpen) {
      setShouldRenderRoot(false);

      onOutAnimationEnd?.();
    }
  });

  const onSpringStart = useCallback(() => {
    activeExitTransitionsCountRef.current += 1;
  }, []);

  const [overlayStyle, overlaySpringApi] = useSpring(
    () => ({
      from: { opacity: 0 },
      onRest: onSpringRest,
      onStart: onSpringStart,
      config: {
        friction: 15,
        clamp: true,
      },
    }),
    []
  );
  const [containerStyle, containerSpringApi] = useSpring(
    () => ({
      from: { opacity: 0, resize: 90 },
      onRest: onSpringRest,
      onStart: onSpringStart,
      config: {
        friction: 15,
        clamp: true,
      },
    }),
    []
  );

  const { handlers: modalClickOutsideHandlers } = useClickOutside({
    callback: onClose,
    isEnabled: isOpen,
  });

  useEffect(() => {
    if (isOpen) {
      overlaySpringApi.start({ opacity: 1 });
      containerSpringApi.start({ resize: 100, opacity: 1 });
    } else {
      overlaySpringApi.start({ opacity: 0 });
      containerSpringApi.start({ resize: 90, opacity: 0 });
    }
  }, [isOpen, overlaySpringApi, containerSpringApi]);

  if (!shouldRenderRoot) {
    return null;
  }

  return createPortal(
    <div className={styles.root}>
      <animated.div className={styles.overlay} style={overlayStyle} />
      <div className={styles.modal}>
        {React.cloneElement(children, {
          ...modalClickOutsideHandlers,
          style: {
            opacity: containerStyle.opacity,
            transform: containerStyle.resize.to(
              (resize) => `scale(${resize / 100})`
            ),
          },
        })}
      </div>
    </div>,
    document.body
  );
};

export { Modal };

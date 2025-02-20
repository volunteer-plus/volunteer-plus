import { useEffect, useRef } from 'react';
import { useEventCallback } from './use-event-callback.hook';

const useClickOutside = ({
  callback,
  isEnabled = true,
}: {
  callback?: () => void;
  isEnabled?: boolean;
} = {}): {
  handlers: {
    onMouseDown: React.MouseEventHandler;
    onTouchStart: React.TouchEventHandler;
  };
  isClickFromOutsideRef: React.MutableRefObject<boolean>;
} => {
  const isClickFromOutsideRef = useRef(true);

  const targetCallback = () => {
    isClickFromOutsideRef.current = false;
  };

  const eventCallback = useEventCallback(callback);

  useEffect(() => {
    if (!isEnabled) {
      return;
    }

    const documentClickHandler = () => {
      if (isClickFromOutsideRef.current) {
        eventCallback();
      }

      isClickFromOutsideRef.current = true;
    };

    document.addEventListener('mousedown', documentClickHandler);
    document.addEventListener('touchstart', documentClickHandler);
    isClickFromOutsideRef.current = true;

    return () => {
      document.removeEventListener('mousedown', documentClickHandler);
      document.removeEventListener('touchstart', documentClickHandler);
    };
  }, [eventCallback, isEnabled]);

  return {
    handlers: {
      onMouseDown: targetCallback,
      onTouchStart: targetCallback,
    },
    isClickFromOutsideRef,
  };
};

export { useClickOutside };

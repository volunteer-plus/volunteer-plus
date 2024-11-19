import { useEffect, useRef } from 'react';
import { useEventCallback } from './use-event-callback.hook';

const useClickOutside = (
  callback: () => void,
  isEnabled: boolean = true
): Pick<React.HTMLAttributes<HTMLElement>, 'onMouseDown' | 'onTouchStart'> => {
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

    return () => {
      document.removeEventListener('mousedown', documentClickHandler);
      document.removeEventListener('touchstart', documentClickHandler);
    };
  }, [eventCallback, isEnabled]);

  return {
    onMouseDown: targetCallback,
    onTouchStart: targetCallback,
  };
};

export { useClickOutside };

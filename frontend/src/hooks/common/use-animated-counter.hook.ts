import { useCallback, useEffect, useRef, useState } from 'react';
import { useStateRef } from '@/hooks/common';

type Params = {
  value: number;
  initialNumber?: number;
  duration?: number;
  framerateLimit?: number;
};

function easeOutExpo(x: number): number {
  return x === 1 ? 1 : 1 - Math.pow(2, -10 * x);
}

const useAnimatedCounter = ({
  value,
  duration = 1000,
  initialNumber = 0,
  framerateLimit = 30,
}: Params): number => {
  const [currentValue, setCurrentValue] = useState(initialNumber);
  const lastAnimationIdRef = useRef<number>(0);

  const currentAnimationIdRef = useRef<number | null>(null);

  const currentValueRef = useStateRef(currentValue);

  const updateAnimationId = useCallback(() => {
    lastAnimationIdRef.current += 1;
    currentAnimationIdRef.current = lastAnimationIdRef.current;
    return lastAnimationIdRef.current;
  }, [lastAnimationIdRef]);

  const startAnimation = useCallback(
    (nextValue: number): void => {
      const animationId = updateAnimationId();

      const previousValue = currentValueRef.current;

      const animationStartTimeStamp = performance.now();
      let lastRenderTimeStamp = animationStartTimeStamp;

      requestAnimationFrame(function step() {
        if (currentAnimationIdRef.current !== animationId) {
          return;
        }

        const currentTimeStamp = performance.now();

        const timeFromLastRender = currentTimeStamp - lastRenderTimeStamp;

        if (timeFromLastRender < 1000 / framerateLimit) {
          requestAnimationFrame(step);
          return;
        }

        const linearProgress =
          (currentTimeStamp - animationStartTimeStamp) / duration;
        const progress = easeOutExpo(linearProgress);

        if (linearProgress >= 1) {
          setCurrentValue(nextValue);
          return;
        }

        setCurrentValue(
          Math.ceil(previousValue + (nextValue - previousValue) * progress)
        );
        lastRenderTimeStamp = currentTimeStamp;

        requestAnimationFrame(step);
      });
    },
    [
      currentAnimationIdRef,
      updateAnimationId,
      duration,
      currentValueRef,
      framerateLimit,
    ]
  );

  useEffect(() => {
    startAnimation(value);
  }, [value, duration, startAnimation]);

  return currentValue;
};

export { useAnimatedCounter };

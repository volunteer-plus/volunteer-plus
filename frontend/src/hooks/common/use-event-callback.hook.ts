import { useCallback, useEffect, useRef } from 'react';

type EventCallback<A extends unknown[], R> = (...args: A) => R;

type UseEventCallback = {
  <A extends unknown[], R>(callback: EventCallback<A, R>): EventCallback<A, R>;
  <A extends unknown[], R>(callback?: EventCallback<A, R>): EventCallback<
    A,
    R | undefined
  >;
};

const useEventCallback: UseEventCallback = <A extends unknown[], R>(
  callback?: EventCallback<A, R>
): EventCallback<A, R> => {
  const callbackRef = useRef(callback);

  useEffect(() => {
    callbackRef.current = callback;
  }, [callback]);

  return useCallback((...args: A) => callbackRef.current?.(...args) as R, []);
};

export { useEventCallback };

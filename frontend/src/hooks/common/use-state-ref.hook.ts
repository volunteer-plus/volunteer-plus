import React, { useEffect, useRef } from 'react';

const useStateRef = <R>(value: R): React.MutableRefObject<R> => {
  const ref = useRef<R>(value);

  useEffect(() => {
    ref.current = value;
  }, [value]);

  return ref;
};

export { useStateRef };

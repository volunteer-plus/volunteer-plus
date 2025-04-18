import { useEffect, useState } from 'react';

import { useEventCallback } from '@/hooks/common';

function useAsyncMemo<T>(
  asyncFn: () => Promise<T>,
  deps: unknown[],
  initialValue: T
): { value: T; isLoading: boolean } {
  const [value, setValue] = useState<T>(initialValue);
  const [isLoading, setIsLoading] = useState(true);

  const asyncFnCallback = useEventCallback(asyncFn);

  useEffect(() => {
    let isMounted = true;

    setIsLoading(true);

    asyncFnCallback()
      .then((result) => {
        if (isMounted) {
          setValue(result);
        }
      })
      .finally(() => {
        if (isMounted) {
          setIsLoading(false);
        }
      });

    return () => {
      isMounted = false;
    };
  }, [...deps, asyncFnCallback]);

  return { value, isLoading };
}

export { useAsyncMemo };

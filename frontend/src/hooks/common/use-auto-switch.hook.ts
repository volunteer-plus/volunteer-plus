import { useEffect, useState } from 'react';
import { useStateRef } from '@/hooks/common';

interface Params<T> {
  frames: T[];
  duration: number;
}

function useAutoSwitch<T>({ duration, frames }: Params<T>) {
  const [index, setIndex] = useState(0);

  const indexRef = useStateRef(index);

  useEffect(() => {
    const interval = setInterval(() => {
      console.log(indexRef.current);
      setIndex((indexRef.current + 1) % frames.length);
    }, duration);

    return () => {
      clearInterval(interval);
    };
  }, [duration, setIndex, frames.length, indexRef]);

  return frames[index];
}

export { useAutoSwitch };

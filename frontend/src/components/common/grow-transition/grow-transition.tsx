import { useEventCallback, useStateRef } from '@/hooks/common';
import { useSpring } from '@react-spring/web';
import { cloneElement, useCallback, useEffect, useState } from 'react';

interface Props {
  on: boolean;
  children: React.ReactElement;
  onOutAnimationFinish?: () => void;
}

interface SpringStyles {
  opacity: number;
  resize: number;
}

const IN_SPRING_STYLES: SpringStyles = {
  opacity: 1,
  resize: 1,
};

const OUT_SPRING_STYLES: SpringStyles = {
  opacity: 0,
  resize: 0.3,
};

const GrowTransition: React.FC<Props> = ({
  on,
  children,
  onOutAnimationFinish: onOutAnimationFinishProp,
}) => {
  const [shouldRenderRoot, setShouldRenderRoot] = useState(on);

  const onRef = useStateRef(on);

  const onOutAnimationFinish = useEventCallback(onOutAnimationFinishProp);

  const onSpringRest = useCallback(() => {
    if (!onRef.current) {
      setShouldRenderRoot(false);
      onOutAnimationFinish();
    }
  }, [onRef, onOutAnimationFinish]);

  const [springStyles, springApi] = useSpring(
    () => ({
      from: OUT_SPRING_STYLES,
      onRest: onSpringRest,
      config: {
        friction: 15,
        clamp: true,
      },
    }),
    []
  );

  useEffect(() => {
    if (on) {
      setShouldRenderRoot(true);
      springApi.start(IN_SPRING_STYLES);
    } else {
      springApi.start(OUT_SPRING_STYLES);
    }
  }, [on, springApi]);

  if (!shouldRenderRoot) {
    return null;
  }

  return cloneElement(children, {
    style: {
      ...children.props.style,
      opacity: springStyles.opacity,
      transform: springStyles.resize.to((resize) => `scale(${resize})`),
    },
  });
};

export { GrowTransition };

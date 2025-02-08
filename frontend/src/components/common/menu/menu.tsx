import { cloneElement, useCallback, useEffect, useState } from 'react';
import { createPortal } from 'react-dom';
import { useSpring } from '@react-spring/web';
import classNames from 'classnames';

import { useEventCallback } from '@/hooks/common';
import { MenuAlignment, MenuCoordinates, MenuSide } from './types';
import { getCoordinateCssValue } from './helpers';
import styles from './styles.module.scss';

interface Props {
  isOpen: boolean;
  targetRef: React.RefObject<HTMLElement>;
  children: React.ReactElement;
  gap?: number;
  side?: MenuSide;
  alignment?: MenuAlignment;
}

const Menu: React.FC<Props> = ({
  isOpen,
  targetRef,
  children,
  gap = 8,
  side = 'bottom',
  alignment = 'center',
}) => {
  const [coordinates, setCoordinates] = useState<MenuCoordinates | null>(null);

  const updateMenuPosition = useCallback(
    ({
      target,
      _gap,
      _side,
      _alignment,
    }: {
      target: HTMLElement;
      _gap: number;
      _side: MenuSide;
      _alignment: MenuAlignment;
    }) => {
      const targetBoundingRect = target.getBoundingClientRect();

      const centerHorizontalAlignmentLeft =
        targetBoundingRect.left + targetBoundingRect.width / 2;
      const centerVerticalAlignmentTop =
        targetBoundingRect.top + targetBoundingRect.height / 2;

      if (_side === 'top') {
        const bottom = window.innerHeight - targetBoundingRect.top + _gap;

        if (_alignment === 'start') {
          setCoordinates({
            left: targetBoundingRect.left,
            bottom,
          });
        } else if (_alignment == 'end') {
          setCoordinates({
            right: window.innerWidth - targetBoundingRect.right,
            bottom,
          });
        } else if (_alignment == 'center') {
          setCoordinates({
            left: centerHorizontalAlignmentLeft,
            bottom,
          });
        } else {
          setCoordinates({
            left: targetBoundingRect.left,
            bottom,
            width: targetBoundingRect.width,
          });
        }
      } else if (_side === 'bottom') {
        const top = targetBoundingRect.bottom + _gap;

        if (_alignment === 'start') {
          setCoordinates({
            left: targetBoundingRect.left,
            top,
          });
        } else if (_alignment == 'end') {
          setCoordinates({
            right: window.innerWidth - targetBoundingRect.right,
            top,
          });
        } else if (_alignment == 'center') {
          setCoordinates({
            left: centerHorizontalAlignmentLeft,
            top,
          });
        } else {
          setCoordinates({
            left: targetBoundingRect.left,
            top,
            width: targetBoundingRect.width,
          });
        }
      } else if (_side === 'left') {
        const right = window.innerWidth - targetBoundingRect.left + _gap;

        if (_alignment === 'start') {
          setCoordinates({
            right,
            top: targetBoundingRect.top,
          });
        } else if (_alignment == 'end') {
          setCoordinates({
            right,
            bottom: window.innerHeight - targetBoundingRect.bottom,
          });
        } else if (_alignment == 'center') {
          setCoordinates({
            right,
            top: centerVerticalAlignmentTop,
          });
        } else {
          setCoordinates({
            right,
            top: targetBoundingRect.top,
            height: targetBoundingRect.height,
          });
        }
      } else if (_side === 'right') {
        const left = targetBoundingRect.right + _gap;

        if (_alignment === 'start') {
          setCoordinates({
            left,
            top: targetBoundingRect.top,
          });
        } else if (_alignment == 'end') {
          setCoordinates({
            left,
            bottom: window.innerHeight - targetBoundingRect.bottom,
          });
        } else if (_alignment == 'center') {
          setCoordinates({
            left,
            top: centerVerticalAlignmentTop,
          });
        } else {
          setCoordinates({
            left,
            top: targetBoundingRect.top,
            height: targetBoundingRect.height,
          });
        }
      } else {
        throw new Error(`Unsupported side: ${_side}`);
      }
    },
    []
  );

  const onSpringRest = useEventCallback(() => {
    if (!isOpen) {
      setCoordinates(null);
    }
  });

  const [containerStyle, containerSpringApi] = useSpring(
    () => ({
      onRest: onSpringRest,
      from: { opacity: 0, resize: 90 },
      config: {
        friction: 5,
        clamp: true,
      },
    }),
    []
  );

  useEffect(() => {
    if (!targetRef.current) {
      return;
    }

    if (isOpen) {
      updateMenuPosition({
        target: targetRef.current,
        _gap: gap,
        _alignment: alignment,
        _side: side,
      });

      containerSpringApi.start({ resize: 100, opacity: 1 });
    } else {
      containerSpringApi.start({ resize: 90, opacity: 0 });
    }
  }, [
    isOpen,
    targetRef,
    gap,
    updateMenuPosition,
    alignment,
    side,
    containerSpringApi,
  ]);

  if (!coordinates) {
    return null;
  }

  return createPortal(
    cloneElement(children, {
      className: classNames(
        children.props.className,
        styles[`${side}Side`],
        styles[`${alignment}Alignment`],
        styles.menu
      ),
      style: {
        ...children.props.style,
        opacity: containerStyle.opacity,
        transform: containerStyle.resize.to(
          (resize) => `scale(${resize / 100})`
        ),
        '--menu-top': getCoordinateCssValue(coordinates.top),
        '--menu-left': getCoordinateCssValue(coordinates.left),
        '--menu-right': getCoordinateCssValue(coordinates.right),
        '--menu-bottom': getCoordinateCssValue(coordinates.bottom),
        '--menu-width': getCoordinateCssValue(coordinates.width),
      },
    }),
    document.body
  );
};

export { Menu };

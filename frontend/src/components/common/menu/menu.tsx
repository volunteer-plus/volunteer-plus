import { cloneElement, useCallback, useEffect, useState } from 'react';
import { createPortal } from 'react-dom';
import classNames from 'classnames';

import { MenuAlignment, MenuCoordinates, MenuSide } from './types';
import styles from './styles.module.scss';
import { getCoordinateCssValue } from './helpers';

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

  useEffect(() => {
    if (isOpen && targetRef.current) {
      updateMenuPosition({
        target: targetRef.current,
        _gap: gap,
        _alignment: alignment,
        _side: side,
      });
    }

    if (!isOpen) {
      setCoordinates(null);
    }
  }, [isOpen, targetRef, gap, updateMenuPosition, alignment, side]);

  if (!isOpen || !coordinates) {
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

import classNames from 'classnames';

import styles from './styles.module.scss';

interface Props extends React.ComponentPropsWithoutRef<'div'> {
  size: 'small' | 'medium';
  color?: string;
  textColor?: string;
}

const Tag: React.FC<Props> = ({
  className,
  size,
  color = 'var(--color-olive-300)',
  textColor = 'var(--color-white)',
  style,
  ...props
}) => {
  return (
    <div
      {...props}
      className={classNames(styles.tag, styles[`${size}Size`], className)}
      style={
        {
          ...style,
          '--tag-color': color,
          '--tag-text-color': textColor,
        } as React.CSSProperties
      }
    />
  );
};

export { Tag };

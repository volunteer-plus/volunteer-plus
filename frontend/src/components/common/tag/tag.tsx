import classNames from 'classnames';

import styles from './styles.module.scss';

interface Props extends React.ComponentPropsWithoutRef<'div'> {
  size: 'small' | 'medium';
  color?: string;
}

const Tag: React.FC<Props> = ({
  className,
  size,
  color = 'var(--color-olive-300)',
  style,
  ...props
}) => {
  return (
    <div
      {...props}
      className={classNames(styles.tag, `${size}Size`, className)}
      style={
        {
          ...style,
          '--tag-color': color,
        } as React.CSSProperties
      }
    />
  );
};

export { Tag };

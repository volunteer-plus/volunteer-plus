import classNames from 'classnames';

import styles from './styles.module.scss';

type Props = Omit<React.ComponentPropsWithoutRef<'div'>, 'children'> & {
  size: number;
  color: string;
  backgroundColor?: string;
  progress: number;
};

const PieProgressIndicator: React.FC<Props> = ({
  className,
  color,
  backgroundColor = 'transparent',
  size,
  progress,
  ...props
}) => {
  const radius = size / 2;
  let angle = progress * 360;

  if (angle === 360) {
    angle = 359.99;
  }

  if (angle === 0) {
    angle = 0.01;
  }

  const endX = radius - radius * Math.sin((angle * Math.PI) / 180);
  const endY = radius - radius * Math.cos((angle * Math.PI) / 180);

  const clipPath = `path('M ${radius} ${radius} L ${radius} 0 A ${radius} ${radius} 0 ${
    angle > 180 ? 1 : 0
  } 0 ${endX} ${endY} Z')`;

  return (
    <div
      {...props}
      className={classNames(styles.pieWrapper, className)}
      style={
        {
          '--pie-color': color,
          '--pie-background-color': backgroundColor,
          '--pie-size': `${size}px`,
          '--pie-clip-path': clipPath,
        } as React.CSSProperties
      }
    >
      <div className={styles.pie} />
    </div>
  );
};

export { PieProgressIndicator };

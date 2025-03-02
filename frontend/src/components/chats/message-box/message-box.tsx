import classNames from 'classnames';
import { animated } from '@react-spring/web';

import styles from './styles.module.scss';

interface Props extends React.ComponentPropsWithoutRef<'div'> {
  backgroundColor: string;
  borderColor: string;
  noPadding?: boolean;
}

const MessageBox: React.FC<Props> = ({
  className,
  style,
  backgroundColor,
  borderColor,
  noPadding = false,
  ...props
}) => {
  return (
    <animated.div
      {...props}
      className={classNames(styles.box, className, {
        [styles.noPadding]: noPadding,
      })}
      style={
        {
          ...style,
          '--message-box-background-color': backgroundColor,
          '--message-box-border-color': borderColor,
        } as React.CSSProperties
      }
    />
  );
};

export { MessageBox };

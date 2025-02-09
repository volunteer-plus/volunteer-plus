import classNames from 'classnames';

import styles from './styles.module.scss';
import { MaterialSymbol } from '../material-symbol';

interface Props extends React.ComponentPropsWithoutRef<'div'> {
  imageSrc?: string;
  size: string;
}

const Avatar: React.FC<Props> = ({ className, imageSrc, size, ...props }) => {
  return (
    <div
      {...props}
      className={classNames(styles.avatar, className)}
      style={
        {
          '--avatar-size': size,
        } as React.CSSProperties
      }
    >
      {imageSrc ? (
        <img src={imageSrc} alt='avatar' className={styles.image} />
      ) : (
        <div className={styles.iconWrapper}>
          <MaterialSymbol className={styles.icon}>person</MaterialSymbol>
        </div>
      )}
    </div>
  );
};

export { Avatar };

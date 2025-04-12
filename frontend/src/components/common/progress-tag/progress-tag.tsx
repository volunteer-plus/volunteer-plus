import classNames from 'classnames';

import { MaterialSymbol, PieProgressIndicator, Tag } from '@/components/common';

import styles from './styles.module.scss';

type Props = Omit<React.ComponentPropsWithoutRef<typeof Tag>, 'color'> & {
  progress?: number | null;
};

const ProgressTag: React.FC<Props> = ({
  children,
  progress = null,
  ...props
}) => {
  return (
    <Tag
      {...props}
      color='var(--color-gray-100)'
      textColor='var(--color-gray-900)'
    >
      <div className={styles.content}>
        {progress === null && (
          <MaterialSymbol className={styles.checkIcon}>check</MaterialSymbol>
        )}
        {progress !== null && (
          <PieProgressIndicator
            size={14}
            color='var(--color-olive-500)'
            backgroundColor='var(--color-gray-50)'
            progress={progress}
          />
        )}
        <div className={styles.text}>{children}</div>
      </div>
    </Tag>
  );
};

export { ProgressTag };

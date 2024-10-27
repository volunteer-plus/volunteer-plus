import classNames from 'classnames';

import styles from './styles.module.scss';
import { Button } from '@/components/button';

const FundraisingItem: React.FC<React.ComponentPropsWithoutRef<'div'>> = ({
  className,
  ...props
}) => {
  return (
    <article {...props} className={classNames(styles.item, className)}>
      <div className={styles.title}>
        На FPV 100 дронів для нищення окупантів
      </div>
      <div className={styles.subtitle}></div>
      <div className={styles.volunteer}>Збирає Васильченеко В. І.</div>
      <div className={styles.actions}>
        <Button>Підтримати</Button>
        <Button variant='outlined'>Про волонтера</Button>
      </div>
    </article>
  );
};

export { FundraisingItem };

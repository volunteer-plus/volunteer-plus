import classNames from 'classnames';
import { Link } from 'react-router-dom';

import { Button, ButtonBase, Tag } from '@/components/common';
import styles from './styles.module.scss';

const FundraisingItem: React.FC<React.ComponentPropsWithoutRef<'div'>> = ({
  className,
  ...props
}) => {
  return (
    <article {...props} className={classNames(styles.item, className)}>
      <h3 className={styles.title}>На FPV 100 дронів для нищення окупантів</h3>
      <div className={styles.subtitle}>
        <Tag size='small'>Дрони</Tag> Для{' '}
        <span className={styles.brigadeName}>3 ОШБ</span>
      </div>
      <div className={styles.volunteer}>
        Збирає <span className={styles.volunteerName}>Васильченеко В. І.</span>
      </div>
      <p className={styles.description}>
        Tenetur qui rerum alias vero laboriosam qui autem cum ut. Porro
        consequatur molestiae quas optio vero similique deserunt laudantium.
        Nulla libero similique illum. Quas velit ut ducimus eum sit consequatur
        quas. Iure amet neque voluptas possimus consequatur tenetur.
      </p>
      <div className={styles.actions}>
        <ButtonBase
          elementType={Link}
          elementProps={{
            children: 'Підтримати',
            to: '/support-fundraising/1',
          }}
        />
        <Button variant='outlined'>Про волонтера</Button>
      </div>
    </article>
  );
};

export { FundraisingItem };

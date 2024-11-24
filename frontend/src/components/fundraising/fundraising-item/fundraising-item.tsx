import classNames from 'classnames';
import { useState } from 'react';

import { Button, Tag } from '@/components/common';
import { SupportFundraisingModal } from '../support-fundraising-modal';
import styles from './styles.module.scss';

const FundraisingItem: React.FC<React.ComponentPropsWithoutRef<'div'>> = ({
  className,
  ...props
}) => {
  const [isSupportModalOpen, setIsSupportModalOpen] = useState(false);

  const onSupportButtonClick = () => {
    setIsSupportModalOpen(true);
  };

  const onSupportModalClose = () => {
    setIsSupportModalOpen(false);
  };

  return (
    <article {...props} className={classNames(styles.item, className)}>
      <h3 className={styles.title}>На FPV 100 дронів для нищення окупантів</h3>
      <div className={styles.subtitle}>
        <Tag>Дрони</Tag> Для <span className={styles.brigadeName}>3 ОШБ</span>
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
        <Button onClick={onSupportButtonClick}>Підтримати</Button>
        <Button variant='outlined'>Про волонтера</Button>
      </div>
      <SupportFundraisingModal
        isOpen={isSupportModalOpen}
        onClose={onSupportModalClose}
      />
    </article>
  );
};

export { FundraisingItem };

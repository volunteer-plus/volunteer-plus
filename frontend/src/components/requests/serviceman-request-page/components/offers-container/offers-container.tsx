import classNames from 'classnames';
import { useState } from 'react';

import { Chat } from '@/components/chats';
import { OffersList } from '@/components/requests';
import { BackButton } from '@/components/common';

import styles from './styles.module.scss';

type Props = React.ComponentPropsWithoutRef<'div'>;

const OffersContainer: React.FC<Props> = ({ className, ...props }) => {
  const [isChatOpen, setIsChatOpen] = useState(false);

  if (isChatOpen) {
    return (
      <div {...props} className={classNames(styles.root, className)}>
        <div className={classNames(styles.titleLine, styles.titleLineMargin)}>
          <h2>Чат із Іваненко І. І.</h2>
          <BackButton onClick={() => setIsChatOpen(false)}>
            Назад до пропозицій
          </BackButton>
        </div>
        <Chat className={styles.chat} />
      </div>
    );
  } else {
    return (
      <div {...props} className={classNames(styles.root, className)}>
        <h2 className={styles.titleLineMargin}>Пропозиції</h2>
        <OffersList
          onOpenChatClick={() => setIsChatOpen(true)}
          className={styles.offersList}
        />
      </div>
    );
  }
};

export { OffersContainer };

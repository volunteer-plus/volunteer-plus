import { useEffect, useState } from 'react';
import { AdminPageTitle } from '@/components/admin';
import { Authenticated } from '@/components/auth';
import {
  Chat,
  ChatNotSelectedPlaceholder,
  ChatsList,
  ChatsListItem,
} from '@/components/chats';

import styles from './styles.module.scss';

const BareChatsPage: React.FC = () => {
  const [isChatSelected, setIsChatSelected] = useState(false);

  const [isUserOnline, setIsUserOnline] = useState(false);

  useEffect(() => {
    const interval = setInterval(() => {
      setIsUserOnline((prev) => !prev);
    }, 3000);

    return () => clearInterval(interval);
  }, []);

  return (
    <main className={styles.main}>
      <div className={styles.leftSide}>
        <div className={styles.titleWrapper}>
          <AdminPageTitle>Чати</AdminPageTitle>
        </div>
        <ChatsList className={styles.chatsList}>
          <ChatsListItem
            path='#'
            userFullName='Петренко Петро Петрович'
            userRole='Волонтер'
            newMessagesCount={3}
            userAvatarImageSrc='https://ichef.bbci.co.uk/images/ic/480xn/p02n576b.jpg'
            onClick={() => setIsChatSelected(!isChatSelected)}
            isActive={isChatSelected}
            isUserOnline={isUserOnline}
          />
          <ChatsListItem
            path='#'
            userFullName='Петренко Петро Петрович'
            userRole='Волонтер'
            newMessagesCount={3}
          />
          <ChatsListItem
            path='#'
            userFullName='Петренко Петро Петрович'
            userRole='Адміністратор'
          />
        </ChatsList>
      </div>
      <div className={styles.rightSide}>
        {isChatSelected ? (
          <Chat className={styles.chat} />
        ) : (
          <ChatNotSelectedPlaceholder className={styles.chatPlaceholder} />
        )}
      </div>
    </main>
  );
};

const ChatsPage = Authenticated(BareChatsPage);

export { ChatsPage };

import { useEffect, useState } from 'react';
import { Authenticated } from '@/components/auth';
import { PageTitle } from '@/components/common';
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
          <PageTitle>Чати</PageTitle>
        </div>
        <ChatsList className={styles.chatsList}>
          <ChatsListItem
            path='#'
            userFullName='Петренко Петро Петрович'
            userRole='Волонтер'
            newMessagesCount={3}
            userAvatarImageSrc='https://i.pinimg.com/474x/98/51/1e/98511ee98a1930b8938e42caf0904d2d.jpg'
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

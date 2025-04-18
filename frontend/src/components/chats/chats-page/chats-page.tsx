import { useParams } from 'react-router-dom';

import { Authenticated } from '@/components/auth';
import { BarsLoader, PageTitle } from '@/components/common';
import {
  Chat,
  ChatNotSelectedPlaceholder,
  ChatsList,
  ChatsListItem,
  NoChatsLabel,
} from '@/components/chats';
import { chatsService } from '@/services/chats/chats';
import { useAsyncMemo } from '@/hooks/common';
import { getFullName } from '@/helpers/user';
import { useUserForAuthenticated } from '@/hooks/auth';

import styles from './styles.module.scss';

const BareChatsPage: React.FC = () => {
  const user = useUserForAuthenticated();

  const { id: selectedRoomId } = useParams();

  const isChatSelected = Boolean(selectedRoomId);

  const { value: rooms, isLoading: areRoomsLoading } = useAsyncMemo(
    async () => {
      return await chatsService.getDirectMessagesRooms({
        userId: user.id,
      });
    },
    [user.id],
    null
  );

  const doRoomsExist = rooms && rooms.length > 0;

  return (
    <main className={styles.main}>
      <div className={styles.leftSide}>
        <div className={styles.titleWrapper}>
          <PageTitle>Чати</PageTitle>
        </div>
        {!doRoomsExist && (
          <div className={styles.chatsListPlaceholder}>
            {areRoomsLoading && <BarsLoader size='50px' />}
            {!areRoomsLoading && <NoChatsLabel />}
          </div>
        )}
        {doRoomsExist && (
          <ChatsList className={styles.chatsList}>
            {rooms?.map((room) => {
              return (
                <ChatsListItem
                  key={room.id}
                  path={`/chats/${room.id}`}
                  userFullName={getFullName(room.user)}
                  newMessagesCount={room.newMessagesCount}
                  isActive={room.id === Number(selectedRoomId)}
                />
              );
            })}
          </ChatsList>
        )}
      </div>
      <div className={styles.rightSide}>
        {selectedRoomId && (
          <Chat className={styles.chat} roomId={Number(selectedRoomId)} />
        )}
        {!isChatSelected && doRoomsExist && (
          <ChatNotSelectedPlaceholder className={styles.chatPlaceholder} />
        )}
      </div>
    </main>
  );
};

const ChatsPage = Authenticated(BareChatsPage);

export { ChatsPage };

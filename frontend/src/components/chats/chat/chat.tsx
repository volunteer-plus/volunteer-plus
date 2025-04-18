import React, { useEffect, useMemo, useRef, useState } from 'react';
import classNames from 'classnames';
import {
  Button,
  GrayContainer,
  MaterialSymbol,
  TextareaField,
} from '@/components/common';
import {
  InboundMessage,
  OutboundMessage,
  TypingIndicator,
} from '@/components/chats';
import { useAsyncMemo } from '@/hooks/common';
import { chatsService } from '@/services/chats/chats';
import { useUserForAuthenticated } from '@/hooks/auth';

import styles from './styles.module.scss';

interface Props extends React.ComponentPropsWithoutRef<typeof GrayContainer> {
  roomId?: number;
}

const Chat: React.FC<Props> = ({ className, roomId, ...props }) => {
  const user = useUserForAuthenticated();

  const messageListRef = useRef<HTMLDivElement>(null);
  const messagesListWrapperRef = useRef<HTMLDivElement>(null);

  const [isSending, setIsSending] = useState(false);

  const { value: room, isLoading: isRoomLoading } = useAsyncMemo(
    async () => {
      if (!roomId) {
        return null;
      }

      return await chatsService.getRoom({
        roomId,
      });
    },
    [roomId],
    null
  );

  const messages = useMemo(() => {
    if (!room) {
      return [];
    }

    return room.messages;
  }, [room]);

  const [messageText, setMessageText] = useState('');

  const sendMessage = async () => {
    setIsSending(true);

    setIsSending(false);
  };

  const isMessageEmpty = messageText.length === 0;

  const onTextareaKeyDown: React.KeyboardEventHandler = (event) => {
    if (event.key === 'Enter' && !event.shiftKey && !isMessageEmpty) {
      event.preventDefault();
      sendMessage();
    }
  };

  useEffect(() => {
    if (!messageListRef.current) {
      return;
    }

    const resizeObserver = new ResizeObserver(() => {
      if (!messagesListWrapperRef.current) {
        return;
      }

      messagesListWrapperRef.current.scrollTo({
        top: messagesListWrapperRef.current.scrollHeight,
      });
    });

    resizeObserver.observe(messageListRef.current);

    return () => {
      resizeObserver.disconnect();
    };
  }, [messages]);

  return (
    <GrayContainer
      {...props}
      className={classNames(styles.root, className)}
      noPadding
    >
      <div className={styles.messagesListWrapper} ref={messagesListWrapperRef}>
        {messages.length === 0 && (
          <div className={styles.noMessagesContainer}>
            <div className={styles.noMessagesMessage}>
              Поки немає повідомлень
            </div>
          </div>
        )}
        <div className={styles.messagesList} ref={messageListRef}>
          {messages.map((message, index) => {
            if (message.fromUser === user.id) {
              return (
                <InboundMessage
                  sentAt={new Date(message.createDate)}
                  key={index}
                >
                  {message.content}
                </InboundMessage>
              );
            } else {
              return (
                <OutboundMessage
                  sentAt={new Date(message.createDate)}
                  key={index}
                >
                  {message.content}
                </OutboundMessage>
              );
            }
          })}
        </div>
        {messages.length > 0 && (
          <TypingIndicator
            isTyping={false}
            className={styles.typingIndicator}
          />
        )}
      </div>
      <div className={styles.textareaLine}>
        <TextareaField
          placeholder='Повідомлення'
          className={styles.messageTextarea}
          rows={1}
          autoSize
          value={messageText}
          onChange={(e) => setMessageText(e.target.value)}
          onKeyDown={onTextareaKeyDown}
        />
        <Button
          className={styles.sendButton}
          disabled={!messageText || isSending || isRoomLoading}
          onClick={() => sendMessage()}
        >
          <MaterialSymbol className={styles.sendIcon}>send</MaterialSymbol>
        </Button>
      </div>
    </GrayContainer>
  );
};

export { Chat };

import React, { useEffect, useRef, useState } from 'react';
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

import styles from './styles.module.scss';
import { sleep } from '@/helpers/common';
import { useStateRef } from '@/hooks/common';

interface Props extends React.ComponentPropsWithoutRef<typeof GrayContainer> {
  userId?: string;
}

const Chat: React.FC<Props> = ({ className, ...props }) => {
  const messageListRef = useRef<HTMLDivElement>(null);
  const messagesListWrapperRef = useRef<HTMLDivElement>(null);

  const [messageText, setMessageText] = useState('');
  const [isTyping, setIsTyping] = useState(false);
  const [messages, setMessages] = useState<
    {
      text: string;
      date: Date;
      isInbound: boolean;
    }[]
  >([]);

  const messagesRef = useStateRef(messages);

  const sendMessage = () => {
    setMessages([
      ...messages,
      {
        date: new Date(),
        isInbound: false,
        text: messageText,
      },
    ]);
    setMessageText('');

    (async () => {
      await sleep(1000);

      setIsTyping(true);

      await sleep(3000);

      setIsTyping(false);

      await sleep(500);

      setMessages([
        ...messagesRef.current,
        {
          date: new Date(),
          isInbound: true,
          text: 'Не пиши мені більше',
        },
      ]);
    })();
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
  }, [messages, isTyping]);

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
            if (message.isInbound) {
              return (
                <InboundMessage sentAt={message.date} key={index}>
                  {message.text}
                </InboundMessage>
              );
            } else {
              return (
                <OutboundMessage sentAt={message.date} key={index}>
                  {message.text}
                </OutboundMessage>
              );
            }
          })}
        </div>
        {messages.length > 0 && (
          <TypingIndicator
            isTyping={isTyping}
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
          disabled={!messageText}
          onClick={() => sendMessage()}
        >
          <MaterialSymbol className={styles.sendIcon}>send</MaterialSymbol>
        </Button>
      </div>
    </GrayContainer>
  );
};

export { Chat };

import classNames from 'classnames';

import styles from './styles.module.scss';
import {
  MessageBox,
  MessageSenderAvatar,
  MessageSentAt,
  MessageText,
} from '@/components/chats';

type Props = React.ComponentPropsWithoutRef<'div'> & {
  onOpenChatClick?: () => void;
  onAcceptClick?: () => void;
  isNew?: boolean;
  volunteerName: string;
  messageText: string;
  messageSentAt: Date;
};

const OfferListItem: React.FC<Props> = ({
  className,
  onAcceptClick,
  onOpenChatClick,
  isNew,
  volunteerName,
  messageText,
  messageSentAt,
  ...props
}) => {
  return (
    <div {...props} className={classNames(styles.root, className)}>
      <MessageSenderAvatar />
      <MessageBox
        backgroundColor='var(--color-white)'
        borderColor='var(--color-gray-100)'
        noPadding
        className={styles.messageBox}
      >
        {isNew && <div className={styles.newLabel}>Нова</div>}
        <div className={styles.data}>
          <div className={styles.volunteerName}>{volunteerName}</div>
          <MessageText className={styles.messageText}>
            {messageText}
          </MessageText>
          <MessageSentAt className={styles.messageSentAt}>
            {messageSentAt}
          </MessageSentAt>
        </div>
        <div className={styles.buttons}>
          <button type='button' onClick={onAcceptClick}>
            Прийняти
          </button>
          <button type='button' onClick={onOpenChatClick}>
            Чат
          </button>
        </div>
      </MessageBox>
    </div>
  );
};

export { OfferListItem };

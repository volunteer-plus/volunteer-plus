import {
  MessageContainer,
  MessageBox,
  MessageSenderAvatar,
  MessageText,
  MessageSentAt,
} from '@/components/chats';
import { GrowTransition } from '@/components/common';

import styles from './styles.module.scss';

type Props = Omit<
  React.ComponentProps<typeof MessageContainer>,
  'alignment' | 'children'
> &
  Pick<React.ComponentPropsWithoutRef<typeof MessageText>, 'children'> & {
    sentAt: Date;
  };

const OutboundMessage: React.FC<Props> = ({ children, sentAt, ...props }) => {
  return (
    <MessageContainer {...props} alignment='right'>
      <GrowTransition on>
        <MessageBox
          borderColor='var(--color-gray-500)'
          backgroundColor='var(--color-olive-300)'
        >
          <MessageText className={styles.text}>{children}</MessageText>
          <MessageSentAt className={styles.sentAt}>{sentAt}</MessageSentAt>
        </MessageBox>
      </GrowTransition>
      <MessageSenderAvatar />
    </MessageContainer>
  );
};

export { OutboundMessage };

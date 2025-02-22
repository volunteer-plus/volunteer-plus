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

const InboundMessage: React.FC<Props> = ({ children, sentAt, ...props }) => {
  return (
    <MessageContainer {...props} alignment='left'>
      <MessageSenderAvatar />
      <GrowTransition on>
        <MessageBox
          borderColor='var(--color-gray-100)'
          backgroundColor='var(--color-white)'
        >
          <MessageText className={styles.text}>{children}</MessageText>
          <MessageSentAt className={styles.sentAt}>{sentAt}</MessageSentAt>
        </MessageBox>
      </GrowTransition>
    </MessageContainer>
  );
};

export { InboundMessage };

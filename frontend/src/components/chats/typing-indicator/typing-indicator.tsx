import {
  MessageBox,
  MessageContainer,
  MessageSenderAvatar,
} from '@/components/chats';
import { GrowTransition } from '@/components/common';
import { useAutoSwitch } from '@/hooks/common';
import { useEffect, useState } from 'react';

interface Props
  extends Omit<
    React.ComponentProps<typeof MessageContainer>,
    'alignment' | 'children'
  > {
  isTyping: boolean;
}

const TypingIndicator: React.FC<Props> = ({ isTyping, ...props }) => {
  const [shouldRender, setShouldRender] = useState(isTyping);

  const dots = useAutoSwitch({
    duration: 400,
    frames: ['', '.', '..', '...'],
  });

  useEffect(() => {
    if (isTyping) {
      setShouldRender(true);
    }
  }, [isTyping]);

  const onBoxOutAnimationFinish = () => {
    setShouldRender(false);
  };

  if (!shouldRender) {
    return false;
  }

  return (
    <MessageContainer {...props} alignment='left'>
      <MessageSenderAvatar />
      <GrowTransition
        on={isTyping}
        onOutAnimationFinish={onBoxOutAnimationFinish}
      >
        <MessageBox
          backgroundColor='var(--color-white)'
          borderColor='var(--color-gray-100)'
        >
          Друкує{dots}
        </MessageBox>
      </GrowTransition>
    </MessageContainer>
  );
};

export { TypingIndicator };

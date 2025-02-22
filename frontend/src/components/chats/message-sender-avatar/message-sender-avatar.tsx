import { Avatar } from '@/components/common';

type Props = Omit<React.ComponentPropsWithoutRef<typeof Avatar>, 'size'>;

const MessageSenderAvatar: React.FC<Props> = (props) => {
  return <Avatar {...props} size='48px' />;
};

export { MessageSenderAvatar };

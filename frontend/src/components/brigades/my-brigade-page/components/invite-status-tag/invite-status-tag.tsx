import { Tag } from '@/components/common';
import { useMemo } from 'react';

interface Props
  extends Omit<
    React.ComponentPropsWithoutRef<typeof Tag>,
    'children' | 'color'
  > {
  status: 'new' | 'revoked' | 'accepted';
}

const InviteStatusTag: React.FC<Props> = ({ status, ...props }) => {
  const text = useMemo(() => {
    switch (status) {
      case 'new':
        return 'Новe';
      case 'revoked':
        return 'Відкликане';
      case 'accepted':
        return 'Прийняте';
    }
  }, [status]);

  const color = useMemo(() => {
    switch (status) {
      case 'new':
        return 'var(--color-warning)';
      case 'revoked':
        return 'var(--color-failure)';
      case 'accepted':
        return 'var(--color-success)';
    }
  }, [status]);

  return (
    <Tag {...props} color={color}>
      {text}
    </Tag>
  );
};

export { InviteStatusTag };

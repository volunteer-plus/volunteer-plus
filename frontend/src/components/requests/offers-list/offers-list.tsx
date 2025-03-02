import classNames from 'classnames';
import { GrayContainer } from '@/components/common';

import styles from './styles.module.scss';
import { OfferListItem } from './components';

type Props = React.ComponentPropsWithoutRef<typeof GrayContainer> & {
  onOpenChatClick: () => void;
};

const OffersList: React.FC<Props> = ({
  className,
  onOpenChatClick,
  ...props
}) => {
  return (
    <GrayContainer
      {...props}
      className={classNames(styles.root, className)}
      noPadding
    >
      <div className={styles.listWrapper}>
        <div className={styles.list}>
          <OfferListItem
            messageSentAt={new Date()}
            messageText='Вітаю! Можемо закрити десь за місяць. Працюємо?'
            volunteerName='Іваненко І. І.'
            onOpenChatClick={onOpenChatClick}
            isNew
          />
          <OfferListItem
            messageSentAt={new Date()}
            messageText='Вітаю! Можемо закрити десь за місяць. Працюємо?'
            volunteerName='Іваненко І. І.'
            onOpenChatClick={onOpenChatClick}
          />
          <OfferListItem
            messageSentAt={new Date()}
            messageText='Вітаю! Можемо закрити десь за місяць. Працюємо?'
            volunteerName='Іваненко І. І.'
            onOpenChatClick={onOpenChatClick}
          />
        </div>
      </div>
    </GrayContainer>
  );
};

export { OffersList };

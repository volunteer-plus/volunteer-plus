import { Link, useParams } from 'react-router-dom';

import { Authenticated } from '@/components/auth';
import { AdminPageContent } from '@/components/admin';
import {
  FieldLabel,
  PageBackButton,
  PageTitle,
  Tag,
  UserItem,
} from '@/components/common';
import { Chat } from '@/components/chats';

import styles from './styles.module.scss';
import { RequestFundraising } from './components';

const BareVolunteerOneRequestPage: React.FC = () => {
  const { id } = useParams();

  const isMyRequest = id === '1234';

  return (
    <AdminPageContent className={styles.root}>
      <PageBackButton
        as={Link}
        to={`/volunteer/requests/${
          isMyRequest ? 'my-requests' : 'available-requests'
        }`}
      >
        Назад до {isMyRequest ? 'моїх' : 'доступних'} запитів
      </PageBackButton>
      <PageTitle>Запит №{id}</PageTitle>
      <div className={styles.content}>
        <div className={styles.infoSide}>
          <div className={styles.fields}>
            <div>
              <div className={styles.requestTitleLine}>
                <h2>Бронежелети</h2>
                <Tag size='small'>Екіпіровка</Tag>
              </div>
              <div className={styles.requestBrigade}>
                Для <span className={styles.brigadeName}>3 ОШБ</span>
              </div>
              <div className={styles.requestDescription}>
                Tenetur qui rerum alias vero laboriosam qui autem cum ut. Porro
                consequatur molestiae quas optio vero similique deserunt
                laudantium. Nulla libero similique illum. Quas velit ut ducimus
                eum s
              </div>
            </div>
            <div>
              <FieldLabel bottomMargin>Відповідальний:</FieldLabel>
              <UserItem
                name='Жмишенко Вадим Альбертович'
                role='Молодший лейтенант'
              />
            </div>
            <div>
              <FieldLabel bottomMargin>Збори:</FieldLabel>
              <RequestFundraising />
            </div>
          </div>
        </div>
        <div className={styles.chatSide}>
          <h2 className={styles.chatTitle}>Чат із Жмишенко В. А. </h2>
          <Chat className={styles.chat} />
        </div>
      </div>
    </AdminPageContent>
  );
};

const VolunteerOneRequestPage = Authenticated(BareVolunteerOneRequestPage, {
  allowedRoles: {
    volunteer: true,
  },
});

export { VolunteerOneRequestPage };

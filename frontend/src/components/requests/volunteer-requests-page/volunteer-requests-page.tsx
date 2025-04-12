import { useCallback, useMemo } from 'react';
import { useNavigate, useParams } from 'react-router-dom';
import _ from 'lodash';

import { AdminPageContent } from '@/components/admin';
import { Authenticated } from '@/components/auth';
import { PageTitle, Tabs } from '@/components/common';

import { VOLUNTEER_REQUESTS_TABS_CONFIGS } from './constants';
import { VolunteerRequestsTab } from './enums';
import {
  AvailableRequestsTabContent,
  MyRequestsTabContent,
} from './components';
import styles from './styles.module.scss';

const BareVolunteerRequestsPage: React.FC = () => {
  const { tab } = useParams();
  const navigate = useNavigate();

  const tabKey = useMemo(() => {
    return _.camelCase(tab);
  }, [tab]);

  const changeTab = useCallback(
    (tabKey: string) => {
      navigate('/volunteer/requests/' + _.kebabCase(tabKey));
    },
    [navigate]
  );

  return (
    <AdminPageContent>
      <PageTitle className={styles.title}>Запити</PageTitle>
      <Tabs
        tabsConfigs={VOLUNTEER_REQUESTS_TABS_CONFIGS}
        activeTabKey={tabKey}
        onTabChange={(key) => changeTab(key as VolunteerRequestsTab)}
      />
      {tabKey === VolunteerRequestsTab.AVAILABLE_REQUESTS && (
        <AvailableRequestsTabContent />
      )}
      {tabKey === VolunteerRequestsTab.MY_REQUESTS && <MyRequestsTabContent />}
    </AdminPageContent>
  );
};

const VolunteerRequestsPage = Authenticated(BareVolunteerRequestsPage, {
  allowedRoles: {
    volunteer: true,
  },
});

export { VolunteerRequestsPage };

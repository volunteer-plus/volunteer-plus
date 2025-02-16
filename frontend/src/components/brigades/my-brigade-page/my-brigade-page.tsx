import React, { useMemo, useState } from 'react';

import { AdminPageContent, AdminPageTitle } from '@/components/admin';
import { Authenticated } from '@/components/auth';
import { TabConfig, Tabs } from '@/components/common';

import {
  BrigadeInvitesTabContent,
  BrigadeMembersTabContent,
  MyBrigadeForm,
} from './components';
import { MyBrigadeTableTab } from './enums';
import styles from './styles.module.scss';

const BareMyBrigadePage: React.FC = () => {
  const [activeTab, setActiveTab] = useState<MyBrigadeTableTab>(
    MyBrigadeTableTab.BRIGADE_MEMBERS
  );

  const tabsConfigs = useMemo<TabConfig[]>(() => {
    return [
      {
        label: 'Члени бригади',
        key: MyBrigadeTableTab.BRIGADE_MEMBERS,
      },
      {
        label: 'Запрошення до бригади',
        key: MyBrigadeTableTab.BRIGADE_INVITES,
      },
    ];
  }, []);

  return (
    <AdminPageContent>
      <AdminPageTitle className={styles.title}>Моя бригада</AdminPageTitle>
      <MyBrigadeForm />
      <div className={styles.tabsContainer}>
        <Tabs
          tabsConfigs={tabsConfigs}
          activeTabKey={activeTab}
          onTabChange={(tab) => setActiveTab(tab as MyBrigadeTableTab)}
        />
        {activeTab === MyBrigadeTableTab.BRIGADE_MEMBERS && (
          <BrigadeMembersTabContent />
        )}
        {activeTab === MyBrigadeTableTab.BRIGADE_INVITES && (
          <BrigadeInvitesTabContent />
        )}
      </div>
    </AdminPageContent>
  );
};

const MyBrigadePage = Authenticated(BareMyBrigadePage, {
  allowedRoles: {
    brigadeAdmin: true,
  },
});

export { MyBrigadePage };

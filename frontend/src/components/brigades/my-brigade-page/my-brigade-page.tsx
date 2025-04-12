import React, { useEffect, useMemo, useState } from 'react';

import { AdminPageContent } from '@/components/admin';
import { Authenticated } from '@/components/auth';
import { TabConfig, Tabs, PageTitle } from '@/components/common';
import { useAppDispatch } from '@/hooks/store';

import {
  BrigadeInvitesTabContent,
  BrigadeMembersTabContent,
  MyBrigadeForm,
} from './components';
import { MyBrigadeTableTab } from './enums';
import styles from './styles.module.scss';
import { loadMyBrigadeIfNotLoaded } from '@/slices/my-brigade';

const BareMyBrigadePage: React.FC = () => {
  const dispatch = useAppDispatch();

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

  useEffect(() => {
    dispatch(loadMyBrigadeIfNotLoaded());
  }, [dispatch]);

  return (
    <AdminPageContent>
      <PageTitle className={styles.title}>Моя бригада</PageTitle>
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

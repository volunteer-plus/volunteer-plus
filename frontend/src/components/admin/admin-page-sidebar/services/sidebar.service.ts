import { User } from '@/types/common';
import {
  isBrigadeAdmin,
  isServiceman,
  isSuperAdmin,
  isVolunteer,
} from '@/helpers/user';

import { SidebarItemConfig } from '../types';

interface ConstructorProps {
  user: User;
}

class SidebarService {
  constructor({ user }: ConstructorProps) {
    this.user = user;
  }

  public getItemsConfigs(): SidebarItemConfig[] {
    const roleSpecificItemsConfigs = this.getRoleSpecificItemsConfigs();
    const allRolesItemsConfigs = this.getAllRolesItemsConfigs();

    return [...roleSpecificItemsConfigs, ...allRolesItemsConfigs];
  }

  private getRoleSpecificItemsConfigs(): SidebarItemConfig[] {
    if (isSuperAdmin(this.user)) {
      return this.getSuperAdminItemsConfigs();
    } else if (isServiceman(this.user)) {
      return this.getServicemanItemsConfigs();
    } else if (isBrigadeAdmin(this.user)) {
      return this.getBrigadeAdminItemsConfigs();
    } else if (isVolunteer(this.user)) {
      return this.getVolunteerItemsConfigs();
    }

    return [];
  }

  private getServicemanItemsConfigs(): SidebarItemConfig[] {
    return [
      {
        key: 'requests',
        iconName: 'conveyor_belt',
        label: 'Запити',
        path: '/serviceman/requests',
        subPaths: [/\/serviceman\/requests\/\d+/],
      },
    ];
  }

  private getVolunteerItemsConfigs(): SidebarItemConfig[] {
    return [
      {
        key: 'requests',
        iconName: 'conveyor_belt',
        label: 'Запити',
        path: '/volunteer/requests/available-requests',
        subPaths: [
          /\/volunteer\/request\/\d+/,
          '/volunteer/requests/my-requests',
        ],
      },
      {
        key: 'fundraising',
        iconName: 'savings',
        label: 'Збори',
        path: '/fundraising-activities',
      },
    ];
  }

  private getSuperAdminItemsConfigs(): SidebarItemConfig[] {
    return [
      {
        key: 'brigades',
        iconName: 'shield',
        label: 'Бригади',
        path: '/brigades',
      },
    ];
  }

  private getBrigadeAdminItemsConfigs(): SidebarItemConfig[] {
    return [
      {
        key: 'my-brigade',
        iconName: 'groups',
        label: 'Моя бригада',
        path: '/my-brigade',
      },
    ];
  }

  private getAllRolesItemsConfigs(): SidebarItemConfig[] {
    return [
      {
        key: 'chats',
        iconName: 'forum',
        label: 'Чати',
        path: '/chats',
      },
      {
        key: 'homepage',
        iconName: 'grid_view',
        label: 'Головна сторінка',
        path: '/',
      },
      {
        key: 'profile',
        iconName: 'person',
        label: 'Профіль',
        path: '/profile',
      },
    ];
  }

  private user: User;
}

export { SidebarService };

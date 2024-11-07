import { User } from '@/types/common';
import { isSuperAdmin } from '@/helpers/user';

import { SidebarItemConfig } from '../types';

interface ConstructorProps {
  user: User;
}

class SidebarService {
  constructor({ user }: ConstructorProps) {
    this.user = user;
  }

  public getItemsConfigs(): SidebarItemConfig[] {
    if (isSuperAdmin(this.user)) {
      return this.getSuperAdminItemsConfigs();
    }

    return [];
  }

  private getSuperAdminItemsConfigs(): SidebarItemConfig[] {
    return [
      {
        key: 'brigades',
        iconName: 'shield',
        label: 'Бригади',
        path: '/brigades',
      },
      {
        key: 'profile',
        iconName: 'person',
        label: 'Профіль',
        path: '/profile',
      },
      {
        key: 'homepage',
        iconName: 'grid_view',
        label: 'Головна сторінка',
        path: '/',
      },
    ];
  }

  private user: User;
}

export { SidebarService };

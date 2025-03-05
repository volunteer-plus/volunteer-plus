import { type TabConfig } from '@/components/common';
import { VolunteerRequestsTab } from '../enums';

const VOLUNTEER_REQUESTS_TABS_CONFIGS: TabConfig[] = [
  {
    key: VolunteerRequestsTab.AVAILABLE_REQUESTS,
    label: 'Доступні запити',
  },
  {
    key: VolunteerRequestsTab.MY_REQUESTS,
    label: 'Мої запити',
  },
];

export { VOLUNTEER_REQUESTS_TABS_CONFIGS };

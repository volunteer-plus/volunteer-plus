interface SidebarItemConfig {
  iconName: string;
  label: string;
  path: string;
  key: string;
  subPaths?: (string | RegExp)[];
}

export type { SidebarItemConfig };

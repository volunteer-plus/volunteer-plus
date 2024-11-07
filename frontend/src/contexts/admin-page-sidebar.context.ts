import React from 'react';

type AdminPageSidebarContextType = {
  isOpen: boolean;
  open: () => void;
  close: () => void;
  toggle: () => void;
};

const AdminPageSidebarContext =
  React.createContext<AdminPageSidebarContextType>({
    isOpen: false,
    open: () => {},
    close: () => {},
    toggle: () => {},
  });

export { AdminPageSidebarContext };

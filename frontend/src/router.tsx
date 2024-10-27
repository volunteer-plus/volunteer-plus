import { createBrowserRouter } from 'react-router-dom';

import { HomePage } from './components';

const router = createBrowserRouter([
  {
    path: '',
    element: <HomePage />,
  },
]);

export { router };

import { store } from '@/store';
import { useDispatch } from 'react-redux';

const useAppDispatch = useDispatch.withTypes<typeof store.dispatch>();

export { useAppDispatch };

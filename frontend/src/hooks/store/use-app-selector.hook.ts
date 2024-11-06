import { useSelector } from 'react-redux';
import { RootState } from '@/types/store';

const useAppSelector = useSelector.withTypes<RootState>();

export { useAppSelector };

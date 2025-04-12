import { useEffect, useMemo } from 'react';

import { SelectOption } from '@/types/common';
import { useAppSelector, useAppDispatch } from '@/hooks/store';
import { loadBrigadeCodesIfNotLoaded } from '@/slices/brigade-codes';

function useBrigadeCodeOptions() {
  const dispatch = useAppDispatch();

  const { data: brigadeCodesData } = useAppSelector(
    (state) => state.brigadeCodes
  );

  const brigadeCodesOptions = useMemo<SelectOption[]>((): SelectOption[] => {
    if (!brigadeCodesData) {
      return [];
    }

    return brigadeCodesData.value.map<SelectOption>((code) => {
      return {
        label: code,
        value: code,
      };
    });
  }, [brigadeCodesData]);

  useEffect(() => {
    dispatch(loadBrigadeCodesIfNotLoaded());
  }, [dispatch]);

  return brigadeCodesOptions;
}

export { useBrigadeCodeOptions };

import { Formik } from 'formik';
import { useMemo } from 'react';
import { Form } from 'react-router-dom';

import { FilterApplyButton, FormikMultiselectField } from '@/components/common';
import { FilterFormContainer } from '@/components/common';

const FilterForm: React.FC = () => {
  const initialValues = useMemo(() => {
    return {
      status: [],
      category: [],
    };
  }, []);

  return (
    <Formik initialValues={initialValues} onSubmit={async () => null}>
      <FilterFormContainer as={Form}>
        <FormikMultiselectField
          name='status'
          label='Статус'
          options={[
            {
              value: 'new',
              label: 'Новий',
            },
            {
              value: 'in_progress',
              label: 'Виконується',
            },
            {
              value: 'done',
              label: 'Виконаний',
            },
            {
              value: 'canceled',
              label: 'Скасований',
            },
          ]}
        />
        <FormikMultiselectField
          name='category'
          label='Категорія'
          options={[
            {
              value: 'медикаменти',
              label: 'Медикаменти',
            },
            {
              value: 'амуніція',
              label: 'Амуніція',
            },
            {
              value: 'дрони',
              label: 'Дрони',
            },
            {
              value: 'набої',
              label: 'Набої',
            },
          ]}
        />
        <FilterApplyButton />
      </FilterFormContainer>
    </Formik>
  );
};

export { FilterForm };

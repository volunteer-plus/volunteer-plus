import { Yup } from '@/yup';

const FORM_VALIDATION_SCHEMA = Yup.object({
  title: Yup.string().required().label('Заголовок'),
  category: Yup.string().required().label('Категорія'),
  amount: Yup.number().positive().required().label('Сума'),
});

export { FORM_VALIDATION_SCHEMA };

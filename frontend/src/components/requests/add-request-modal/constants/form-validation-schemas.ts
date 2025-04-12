import { Yup } from '@/yup';
import { DELIVERY_DATE_FIELD_ASAP_VALUE } from '@/components/requests';

const FORM_VALIDATION_SCHEMA = Yup.object({
  title: Yup.string().required().label('Заголовок'),
  category: Yup.string().required().label('Категорія'),
  deliveryDate: Yup.mixed().when(([value], schema) => {
    if (value === DELIVERY_DATE_FIELD_ASAP_VALUE) {
      return schema;
    }

    return Yup.date().required().label('Крайній термін поставки');
  }),
});

export { FORM_VALIDATION_SCHEMA };

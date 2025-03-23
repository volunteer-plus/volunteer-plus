import { Yup } from '@/yup';

const ADD_BRIGADE_FORM_VALIDATION_SCHEMA = Yup.object({
  name: Yup.string().required().label('Назва бригади'),
  regimentCode: Yup.string().required().label('Код бригади'),
  adminEmail: Yup.string()
    .email()
    .required()
    .label('Email адміністратора бригади'),
  description: Yup.string().label('Опис бригади'),
});

export { ADD_BRIGADE_FORM_VALIDATION_SCHEMA };

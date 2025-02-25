import * as Yup from 'yup';

Yup.setLocale({
  mixed: {
    required: "${label} це обов'язкове поле",
    notType: 'Некоректний формат',
  },
  number: {
    integer: '${label} має бути цілим числом',
    min: '${label} має бути не менше ніж ${min}',
    positive: '${label} має бути більше нуля',
  },
  string: {
    email: 'Введіть коректний email',
  },
});

export { Yup };

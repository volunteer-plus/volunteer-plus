import * as Yup from 'yup';

Yup.setLocale({
  mixed: {
    required: "${label} це обов'язкове поле",
  },
  number: {
    integer: '${label} має бути цілим числом',
    min: '${label} має бути не менше ніж ${min}',
  },
});

export { Yup };

import * as Yup from 'yup';

Yup.setLocale({
  mixed: {
    required: "${label} це обов'язкове поле",
  },
});

export { Yup };

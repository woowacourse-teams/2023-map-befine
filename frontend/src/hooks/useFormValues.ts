import { useState } from 'react';
import { validateCurse, validatePolitically } from '../validations';

const initErrorMessages = <T extends Record<keyof T, string>>(
  initValues: T,
) => {
  const initKeys = Object.keys(initValues) as (keyof T)[];

  return initKeys.reduce(
    (acc, key) => {
      acc[key] = '';
      return acc;
    },
    {} as Record<keyof T, string>,
  );
};

const useFormValues = <T extends Record<keyof T, string>>(initValues: T) => {
  const [formValues, setFormValues] = useState<T>(initValues);
  const [errorMessages, setErrorMessages] = useState<Record<keyof T, string>>(
    initErrorMessages(initValues),
  );

  const onChangeInput = (
    e: React.ChangeEvent<HTMLTextAreaElement | HTMLInputElement>,
    isRequired: boolean,
    maxLength?: number,
  ) => {
    const { name, value } = e.target;

    if (value.length - 1 === maxLength) {
      return;
    }

    setFormValues((prevValues: T) => ({
      ...prevValues,
      [name]: value,
    }));

    if (isRequired && value.length === 0) {
      setErrorMessages((prev) => ({ ...prev, [name]: '필수 입력 값입니다.' }));
      return;
    }

    if (validateCurse(value) || validatePolitically(value)) {
      setErrorMessages((prev) => ({
        ...prev,
        [name]: '사용할 수 없는 단어가 포함되어 있습니다.',
      }));
      return;
    }

    setErrorMessages((prev) => ({ ...prev, [name]: '' }));
  };

  return { formValues, errorMessages, setFormValues, onChangeInput };
};

export default useFormValues;

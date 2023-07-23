import { useState } from 'react';

const useFormValues = <T>(initValues: T) => {
  const [formValues, setFormValues] = useState<T>(initValues);

  const onChangeInput = (
    e: React.ChangeEvent<HTMLTextAreaElement | HTMLInputElement>,
  ) => {
    const { name, value } = e.target;
    setFormValues((prevValues: T) => ({
      ...prevValues,
      [name]: value,
    }));
  };

  return { formValues, setFormValues, onChangeInput };
};

export default useFormValues;

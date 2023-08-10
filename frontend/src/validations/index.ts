const REG_EXP_CURSES = /(시발|씨발|병신|호로|새끼|개병신|좆|유효성)/;
const REG_EXP_POLITICALLY = /(월북|일베|빨갱이|쪽바리|짱깨)/;

export const validateCurse = (userInput: string) => {
  return REG_EXP_CURSES.test(userInput);
};

export const validatePolitically = (userInput: string) => {
  return REG_EXP_POLITICALLY.test(userInput);
};

export const hasErrorMessage = <T extends object>(errorMessages: T) => {
  return Object.values(errorMessages).some(
    (errorMessage) => errorMessage.length > 0,
  );
};

export const hasNullValue = <T extends object>(
  formValues: T,
  notRequiredKey?: keyof T,
) => {
  return Object.entries(formValues).some(([key, value]) => {
    if (notRequiredKey && key === notRequiredKey) return false;
    return value.length === 0;
  });
};

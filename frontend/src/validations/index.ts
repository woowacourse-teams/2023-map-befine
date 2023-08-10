const REG_EXP_CURSES =
  /(간나새끼|개새끼|개새|개쓰레기|개소리|개씨발|개씹|지랄|좆|남창|느금마|니미럴|니애미|니애비|똘추|따까리|미친새끼|미친놈|미친년|병신|븅딱|빠구리|빨통|뻐큐|쌍놈|썅놈|쌍년|썅년|쌍노무새끼|썅노무새끼|시발|씨바|씨발|시팔|씨팔|씨부랄|시부랄|씹년|씹새끼|씹새|씹창|애새끼|애미뒤진|애미 뒤진|애비뒤진|애비 뒤진|엠창|육변기|좆|지랄|제기랄|창녀|창남|창놈|호로)/;
const REG_EXP_POLITICALLY =
  /(괴뢰|빨갱이|왜놈|일베|조센징|쪽바리|짱깨|월북|매국노|메갈|섹스|쎅쓰|쎅스|섹쓰|자지|보지)/;

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

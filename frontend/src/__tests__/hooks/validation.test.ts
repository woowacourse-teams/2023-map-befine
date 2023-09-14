import { renderHook } from '@testing-library/react';
import {
  hasErrorMessage,
  hasNullValue,
  validateCurse,
  validatePolitically,
} from '../../validations';
import useFormValues from '../../hooks/useFormValues';
import { act } from 'react-dom/test-utils';

interface FormValuesProps {
  name: string;
  description: string;
}

describe('사용자 입력 유효성 검사 테스트', () => {
  test('validateCure 함수는 사용자가 욕설을 입력할 경우 true를 반환한다.', () => {
    const USER_INPUT = '개새끼';

    const result = validateCurse(USER_INPUT);

    expect(result).toBe(true);
  });

  test('validateCure 함수는 사용자가 입력한 값 중에 욕설이 포함되어 있으면 true를 반환한다.', () => {
    const USER_INPUT = '달동네 개쓰레기들 모음';

    const result = validateCurse(USER_INPUT);

    expect(result).toBe(true);
  });

  test('validatePolitically 함수는 사용자가 사회적 문제의 단어를 입력한 경우 true를 반환한다.', () => {
    const USER_INPUT = '월북';

    const result = validatePolitically(USER_INPUT);

    expect(result).toBe(true);
  });

  test('validatePolitically 함수는 사용자가 입력한 갑 중에 사회적 문제의 단어가 포함되어 있으면 true를 반환한다.', () => {
    const USER_INPUT = '괴뢰군 2023년 최신 루트';

    const result = validatePolitically(USER_INPUT);

    expect(result).toBe(true);
  });

  test('hasErrorMessage 함수는 사용자가 입력한 값 중 하나라도 유효성 검사에 걸리면 true를 반환한다.', () => {
    const { result } = renderHook(() =>
      useFormValues<FormValuesProps>({
        name: '',
        description: '',
      }),
    );
    const userInput = {
      target: {
        name: 'name',
        value: '개새끼',
      },
    } as React.ChangeEvent<HTMLTextAreaElement | HTMLInputElement>;

    act(() => {
      result.current.onChangeInput(userInput, true, 20);
    });

    const hasErrorMessageResult = hasErrorMessage(result.current.errorMessages);

    expect(hasErrorMessageResult).toBe(true);
  });

  test('hasErrorMessage 함수는 필수값 항목을 입력하지 않았을 경우 true를 반환한다.', () => {
    const { result } = renderHook(() =>
      useFormValues<FormValuesProps>({
        name: '',
        description: '',
      }),
    );
    const userInput = {
      target: {
        name: 'name',
        value: '',
      },
    } as React.ChangeEvent<HTMLTextAreaElement | HTMLInputElement>;

    act(() => {
      result.current.onChangeInput(userInput, true, 20);
    });

    const hasErrorMessageResult = hasErrorMessage(result.current.errorMessages);

    expect(hasErrorMessageResult).toBe(true);
  });

  test('hasErrorMessage 함수는 필수값이 아닌 항목을 입력하지 않으면 false를 반환한다.', () => {
    const { result } = renderHook(() =>
      useFormValues<FormValuesProps>({
        name: '',
        description: '',
      }),
    );
    const userInput = {
      target: {
        name: 'name',
        value: '',
      },
    } as React.ChangeEvent<HTMLTextAreaElement | HTMLInputElement>;

    act(() => {
      result.current.onChangeInput(userInput, false, 20);
    });

    const hasErrorMessageResult = hasErrorMessage(result.current.errorMessages);

    expect(hasErrorMessageResult).toBe(false);
  });

  test('hasNullValue 함수는 사용자가 아무것도 입력하지 않았을 경우 true를 반환한다.', () => {
    const formValues = {
      name: '',
      description: '',
    };

    const result = hasNullValue(formValues);

    expect(result).toBe(true);
  });

  test('hasNullValue 함수는 사용자가 지정한 키의 값은 빈 값으로 두어도 false를 반환한다.', () => {
    const formValues = {
      name: '',
      description: '선릉역 맛집 리스트입니다.',
    };

    const result = hasNullValue(formValues, 'name');

    expect(result).toBe(false);
  });
});

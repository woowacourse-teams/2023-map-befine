import { renderHook } from '@testing-library/react';
import useFormValues from '../../hooks/useFormValues';
import { act } from 'react-dom/test-utils';

interface FormValuesProps {
  name: string;
  description: string;
}

describe('useFormValues 훅 초기화 및 수정 테스트', () => {
  test('매개변수로 받은 초기값을 정상적으로 반환하는지 확인한다.', () => {
    const { result } = renderHook(() =>
      useFormValues<FormValuesProps>({
        name: '선릉',
        description: '선릉이란 무엇일까요?',
      }),
    );

    expect(result.current.formValues.name).toBe('선릉');
  });

  test('setFormValues를 통해 set할 수 있는지 확인하다.', () => {
    const { result } = renderHook(() =>
      useFormValues<FormValuesProps>({
        name: '잠실역 주변 맛집',
        description: '선릉역 주변에서 먹을 만한 곳들 모음집입니다.',
      }),
    );

    act(() => {
      result.current.setFormValues((prevState) => ({
        ...prevState,
        description: '잠실역 주변에서 맛있고 유명한 곳들을 모아봤습니다.',
      }));
    });

    expect(result.current.formValues.description).toBe(
      '잠실역 주변에서 맛있고 유명한 곳들을 모아봤습니다.',
    );
  });

  test('입력 받은 초기값 타입 형식에 맞게 에러 객체를 반환하는지 확인한다.', () => {
    const { result } = renderHook(() =>
      useFormValues<FormValuesProps>({
        name: '혼자 돌기 좋은 서울 산책로',
        description:
          '조용하고 혼자 사색에 잠겨 산책할 수 있는 코스들 모음입니다.',
      }),
    );

    expect(result.current.errorMessages).toEqual({ name: '', description: '' });
  });
});

import { renderHook } from '@testing-library/react';
import useFormValues from '../../hooks/useFormValues';
import { act } from 'react-dom/test-utils';

describe('useFormValues 테스트', () => {
  test('매개변수로 받은 초기값을 정상적으로 반환하는지 확인한다.', () => {
    const { result } = renderHook(() => useFormValues<string>('토픽 이름'));

    expect(result.current.formValues).toBe('토픽 이');
  });

  test('setFormValues를 통해 set할 수 있는지 확인하다.', () => {
    const { result } = renderHook(() => useFormValues<string>('토픽 이름'));

    act(() => {
      result.current.setFormValues('인기 있는 토픽');
    });

    expect(result.current.formValues).toBe('인기 있는 토픽');
  });
});

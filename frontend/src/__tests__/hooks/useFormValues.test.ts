import { renderHook, screen } from '@testing-library/react';
import useFormValues from '../../hooks/useFormValues';

describe('useFormValues 테스트', () => {
  test('매개변수로 받은 초기값을 정상적으로 반환하는지 확인', () => {
    const { result } = renderHook(() => useFormValues<string>('토픽 이름'));

    expect(result.current.formValues).toBe('토픽 이름');
  });
});

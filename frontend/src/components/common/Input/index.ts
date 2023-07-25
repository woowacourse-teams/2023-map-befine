import { styled } from 'styled-components';

const Input = styled.input`
  color: ${({ theme }) => theme.color.black};
  border: 1px solid ${({ theme }) => theme.color.black};
  padding: ${({ theme }) => theme.spacing['2']};
  box-sizing: border-box;
  width: 100%;
  height: 52px;
  font-size: ${({ theme }) => theme.fontSize.small};
  border-radius: ${({ theme }) => theme.radius.small};

  &::placeholder {
    color: ${({ theme }) => theme.color.gray};
  }

  input[type='number']::-webkit-outer-spin-button,
  input[type='number']::-webkit-inner-spin-button {
    appearance: none;
    margin: 0;
  }
`;

export default Input;

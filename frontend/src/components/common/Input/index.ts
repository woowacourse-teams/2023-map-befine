import { styled } from 'styled-components';

const Input = styled.input`
  width: 100%;
  height: 52px;
  box-sizing: border-box;
  color: ${({ theme }) => theme.color.black};
  border: 1px solid ${({ theme }) => theme.color.black};
  padding: ${({ theme }) => theme.spacing['2']};
  font-size: ${({ theme }) => theme.fontSize.small};
  border-radius: ${({ theme }) => theme.radius.small};

  &::placeholder {
    color: ${({ theme }) => theme.color.gray};
  }
`;

export default Input;

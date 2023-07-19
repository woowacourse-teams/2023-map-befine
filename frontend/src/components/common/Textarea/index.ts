import { styled } from 'styled-components';

const Textarea = styled.textarea`
  width: 100%;
  height: 100px;
  color: ${({ theme }) => theme.color.black};
  border: 1px solid ${({ theme }) => theme.color.black};
  border-radius: ${({ theme }) => theme.radius.small};
  padding: ${({ theme }) => theme.spacing['2']};
  box-sizing: border-box;
  font-size: ${({ theme }) => theme.fontSize.small};
  resize: none;
  letter-spacing: -0.3%;
  line-height: 160%;

  &::placeholder {
    color: ${({ theme }) => theme.color.gray};
  }
`;

export default Textarea;

import { styled } from 'styled-components';

interface TextareaProps {
  hasBadWord: boolean;
}

const Textarea = styled.textarea<TextareaProps>`
  width: 100%;
  height: 100px;
  box-sizing: border-box;
  resize: none;
  line-height: 160%;
  color: ${({ theme }) => theme.color.black};
  border: 1px solid ${({ theme }) => theme.color.black};
  border-radius: ${({ theme }) => theme.radius.small};
  padding: ${({ theme }) => theme.spacing['2']};
  font-size: ${({ theme }) => theme.fontSize.small};
  outline: ${({ hasBadWord }) => hasBadWord && 'solid 1px #FF4040'};

  &::placeholder {
    color: ${({ theme }) => theme.color.gray};
  }
`;

export default Textarea;

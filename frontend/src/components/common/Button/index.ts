import styled from 'styled-components';
import theme from '../../../themes';

export type ButtonVariant = 'primary' | 'secondary';

export type ButtonProps = React.ButtonHTMLAttributes<HTMLButtonElement> & {
  variant: ButtonVariant;
  children: React.ReactNode;
};

const variants = {
  primary: {
    color: `${theme.color.white}`,
    backgroundColor: `${theme.color.primary}`,
    border: `1px solid ${theme.color.primary}`,
  },
  secondary: {
    color: `${theme.color.primary}`,
    backgroundColor: 'transparent',
    border: `1px solid ${theme.color.primary}`,
  },
};

const Button = styled.button<ButtonProps>`
  width: max-content;
  cursor: pointer;
  color: ${({ variant }) => variants[variant].color};
  background-color: ${({ variant }) => variants[variant].backgroundColor};
  border: ${({ variant }) => variants[variant].border};
  opacity: ${({ disabled }) => (disabled ? 0.5 : 1)};
  border-radius: ${({ theme }) => theme.radius.small};
  padding: ${({ theme }) => `${theme.spacing['2']} ${theme.spacing['3']}`};
  font-size: ${({ theme }) => theme.fontSize.small};
  box-sizing: border-box;

  &:hover {
    filter: brightness(0.9);
  }
`;

export default Button;

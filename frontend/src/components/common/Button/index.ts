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
    pseudoClass: {
      hover: {
        backgroundColor: `transparent`,
        color: `${theme.color.primary}`,
      },
      disabled: {
        backgroundColor: `${theme.color.primary}`,
        opacity: 0.5,
      },
    },
  },
  secondary: {
    color: `${theme.color.primary}`,
    backgroundColor: 'transparent',
    border: `1px solid ${theme.color.primary}`,
    pseudoClass: {
      hover: {
        backgroundColor: `${theme.color.primary}`,
        color: `${theme.color.white}`,
      },
      disabled: {
        backgroundColor: `${theme.color.white}`,
        opacity: 0.5,
      },
    },
  },
};

const Button = styled.button<ButtonProps>`
  width: max-content;
  cursor: pointer;
  color : ${({ variant }) => variants[variant].color};
  background-color: ${({ variant }) => variants[variant].backgroundColor};
  border: ${({ variant }) => variants[variant].border};
  opacity: ${({ disabled }) => (disabled ? 0.5 : 1)};
  border-radius: ${({ theme }) => theme.radius.small};
  padding: ${({ theme }) => theme.spacing['2']} ${({ theme }) => theme.spacing['3']};
  box-sizing: border-box;
  
  &:hover{
    background-color: ${({ variant }) => variants[variant].pseudoClass.hover.backgroundColor};
    color: ${({ variant }) => variants[variant].pseudoClass.hover.color};
  }
`;

export default Button;

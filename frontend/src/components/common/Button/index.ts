import styled from 'styled-components';

import theme from '../../../themes';

export type ButtonVariant = 'primary' | 'secondary' | 'custom';

export interface ButtonProps
  extends React.ButtonHTMLAttributes<HTMLButtonElement> {
  variant: ButtonVariant;
  children: React.ReactNode;
}

const variants = {
  primary: {
    color: `${theme.color.white}`,
    backgroundColor: `${theme.color.primary}`,
    border: `1px solid ${theme.color.primary}`,
    padding: `${theme.spacing['2']} ${theme.spacing['3']}`,
  },
  secondary: {
    color: `${theme.color.primary}`,
    backgroundColor: 'transparent',
    border: `1px solid ${theme.color.primary}`,
    padding: `${theme.spacing['2']} ${theme.spacing['3']}`,
  },
  custom: {
    color: `${theme.color.white}`,
    backgroundColor: `${theme.color.primary}`,
    border: `1px solid ${theme.color.primary}`,
    padding: `${theme.spacing['0']} ${theme.spacing['2']}`,
  },
};

const Button = styled.button<ButtonProps>`
  width: max-content;
  cursor: pointer;
  box-sizing: border-box;
  color: ${({ variant }) => variants[variant].color};
  background-color: ${({ variant }) => variants[variant].backgroundColor};
  border: ${({ variant }) => variants[variant].border};
  opacity: ${({ disabled }) => (disabled ? 0.5 : 1)};
  border-radius: ${({ theme }) => theme.radius.small};
  padding: ${({ variant }) =>
    variants[variant].padding || `${theme.spacing['2']} ${theme.spacing['3']}`};
  font-size: ${({ theme }) => theme.fontSize.small};

  &:hover {
    filter: brightness(0.9);
  }
`;

export default Button;

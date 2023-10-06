import { ReactNode } from 'react';
import styled from 'styled-components';

const TooltipContainer = styled.div`
  display: flex;
  justify-content: center;
`;

const TooltipText = styled.div<{ position: string }>`
  background-color: ${({ theme }) => theme.color.darkGray};
  border-radius: ${({ theme }) => theme.radius.small};
  bottom: ${({ position }) => (position === 'top' ? '150%' : '')};
  color: #fff;
  opacity: 0;
  padding: ${({ theme }) => `${theme.spacing['2']} ${theme.spacing['3']}`};
  position: absolute;
  text-align: center;
  visibility: hidden;
  z-index: 1;

  ${({ position }) => (position === 'top' ? 'translateX(-50%)' : '')};

  ${TooltipContainer}:hover & {
    opacity: 1;
    visibility: visible;

    animation: fadeout 2s;
    -moz-animation: fadeout 2s; /* Firefox */
    -webkit-animation: fadeout 2s; /* Safari and Chrome */
    -o-animation: fadeout 2s; /* Opera */
    animation-fill-mode: forwards;
  }
  @keyframes fadeout {
    from {
      opacity: 1;
    }
    to {
      opacity: 0;
    }
  }
  @-moz-keyframes fadeout {
    /* Firefox */
    from {
      opacity: 1;
    }
    to {
      opacity: 0;
    }
  }
  @-webkit-keyframes fadeout {
    /* Safari and Chrome */
    from {
      opacity: 1;
    }
    to {
      opacity: 0;
    }
  }
  @-o-keyframes fadeout {
    /* Opera */
    from {
      opacity: 1;
    }
    to {
      opacity: 0;
    }
  }
`;

interface TooltipProps {
  content: string;
  position?: 'top' | 'bottom' | 'right' | 'left';
  children: ReactNode;
}

function Tooltip({ children, content, position = 'top' }: TooltipProps) {
  return (
    <TooltipContainer>
      {children}
      <TooltipText position={position}>{content}</TooltipText>
    </TooltipContainer>
  );
}

export default Tooltip;

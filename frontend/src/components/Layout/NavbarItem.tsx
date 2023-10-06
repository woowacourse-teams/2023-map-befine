import { FunctionComponent } from 'react';
import styled from 'styled-components';

import useKeyDown from '../../hooks/useKeyDown';
import Text from '../common/Text';
import SeeTogetherCounter from '../SeeTogetherCounter';

interface NavbarItemProps {
  label: string;
  icon: FunctionComponent;
  focusIcon: FunctionComponent;
  isHighlighted?: boolean;
  onClick: () => void;
  $layoutWidth: '100vw' | '372px';
}

function NavbarItem({
  label,
  icon: Icon,
  focusIcon: FocusIcon,
  isHighlighted = false,
  onClick,
  $layoutWidth,
}: NavbarItemProps) {
  const { elementRef, onElementKeyDown } = useKeyDown<HTMLDivElement>();

  return (
    <IconWrapper
      onClick={onClick}
      tabIndex={10}
      ref={elementRef}
      onKeyDown={onElementKeyDown}
      $layoutWidth={$layoutWidth}
    >
      {isHighlighted ? <FocusIcon /> : <Icon />}
      <Text
        color={isHighlighted ? 'primary' : 'darkGray'}
        $fontSize="extraSmall"
        $fontWeight="normal"
      >
        {label}
      </Text>
      {label === '모아보기' ? <SeeTogetherCounter /> : null}
    </IconWrapper>
  );
}

const IconWrapper = styled.div<{ $layoutWidth: '100vw' | '372px' }>`
  position: relative;
  display: flex;
  flex-direction: column;
  align-items: center;
  width: 52px;
  cursor: pointer;
  margin-right: ${({ $layoutWidth }) =>
    $layoutWidth === '100vw' ? '48px' : '0'};

  &:last-of-type {
    margin-right: 0;
  }

  @media (max-width: 1076px) {
    margin-right: 0;
  }
`;

export default NavbarItem;

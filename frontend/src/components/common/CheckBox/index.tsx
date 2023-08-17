import { useState } from 'react';
import styled from 'styled-components';
import useKeyDown from '../../../hooks/useKeyDown';
import Flex from '../Flex';

interface CheckboxProps {
  id: number;
  isAlreadyChecked: boolean;
  label: string;
  onChecked: (checked: boolean, id: number) => void;
}

const Checkbox = ({
  id,
  isAlreadyChecked,
  label,
  onChecked,
}: CheckboxProps) => {
  const [isChecked, setIsChecked] = useState(isAlreadyChecked);
  const { elementRef, onElementKeyDown } = useKeyDown<HTMLInputElement>();

  const updateCheckedMembers = () => {
    const updatedChecked = !isChecked;
    setIsChecked(updatedChecked);
    onChecked(updatedChecked, id);
  };

  return (
    <CheckboxWrapper key={id}>
      <Label htmlFor={label}>
        <Flex $justifyContent="space-between" width="100%">
          <span>{label}</span>
          <CheckboxInput
            type="checkbox"
            id={label}
            checked={isChecked}
            onChange={updateCheckedMembers}
            tabIndex={6}
            ref={elementRef}
            onKeyDown={onElementKeyDown}
          />
        </Flex>
      </Label>
    </CheckboxWrapper>
  );
};

const CheckboxWrapper = styled(Flex)`
  border-bottom: 1px solid #c6c6c6;
  margin-bottom: 20px;

  &:last-child {
    border-bottom: none;
    margin-bottom: 0;
  }
`;

const Label = styled.label`
  cursor: pointer;
  display: flex;
  align-items: center;
  width: 100%;
`;

const CheckboxInput = styled.input`
  -webkit-appearance: none;
  appearance: none;
  width: 1.6em;
  height: 1.6em;
  border-radius: 0.15em;
  margin-right: 0.5em;
  border: 0.15em solid ${({ theme }) => theme.color.primary};
  background-color: ${({ theme }) => theme.color.white};
  outline: none;
  cursor: pointer;

  &:checked {
    border-color: transparent;
    background-image: url("data:image/svg+xml,%3csvg viewBox='0 0 16 16' fill='white' xmlns='http://www.w3.org/2000/svg'%3e%3cpath d='M5.707 7.293a1 1 0 0 0-1.414 1.414l2 2a1 1 0 0 0 1.414 0l4-4a1 1 0 0 0-1.414-1.414L7 8.586 5.707 7.293z'/%3e%3c/svg%3e");
    background-size: 100% 100%;
    background-position: 50%;
    background-repeat: no-repeat;
    background-color: ${({ theme }) => theme.color.primary};
  }
`;

export default Checkbox;

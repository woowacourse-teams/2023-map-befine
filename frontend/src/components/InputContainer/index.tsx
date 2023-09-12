import { styled } from 'styled-components';
import Flex from '../common/Flex';
import Input from '../common/Input';
import Space from '../common/Space';
import Text from '../common/Text';
import Textarea from '../common/Textarea';
import { ForwardedRef, forwardRef } from 'react';
import Box from '../common/Box';

interface InputContainerProps {
  tagType: 'input' | 'textarea';
  containerTitle: string;
  isRequired: boolean;
  name: string;
  value: string;
  placeholder: string;
  onChangeInput: (
    e: React.ChangeEvent<HTMLTextAreaElement | HTMLInputElement>,
    isRequired: boolean,
    maxLength?: number,
  ) => void;
  tabIndex: number;
  errorMessage: string;
  readOnly?: boolean;
  autoFocus?: boolean;
  maxLength?: number;
}

const InputContainer = ({
  tagType,
  containerTitle,
  isRequired,
  name,
  value,
  placeholder,
  onChangeInput,
  tabIndex,
  errorMessage,
  readOnly,
  autoFocus,
  maxLength,
}: InputContainerProps) => {
  const onChangeUserInput = (
    e: React.ChangeEvent<HTMLInputElement | HTMLTextAreaElement>,
  ) => {
    if (maxLength) {
      onChangeInput(e, isRequired, maxLength);
      return;
    }

    onChangeInput(e, isRequired);
  };

  return (
    <>
      <Flex>
        <Text color="black" $fontSize="default" $fontWeight="normal">
          {containerTitle}
        </Text>
        {isRequired && (
          <>
            <Space size={0} />
            <Text color="primary" $fontSize="extraSmall" $fontWeight="normal">
              *
            </Text>
          </>
        )}
      </Flex>
      <Space size={0} />
      {tagType === 'input' ? (
        <Box position="relative">
          <Input
            name={name}
            value={value}
            placeholder={placeholder}
            onChange={onChangeUserInput}
            tabIndex={tabIndex}
            $hasBadWord={errorMessage.length > 0}
            readOnly={readOnly}
            autoFocus={autoFocus}
            maxLength={maxLength}
          />
          {maxLength && (
            <CurrentLengthOfInput>
              {value.length}/{maxLength}
            </CurrentLengthOfInput>
          )}
        </Box>
      ) : (
        <Box position="relative">
          <Textarea
            name={name}
            value={value}
            placeholder={placeholder}
            onChange={onChangeUserInput}
            tabIndex={tabIndex}
            $hasBadWord={errorMessage.length > 0}
            maxLength={maxLength}
          />
          {maxLength && (
            <CurrentLengthOfTextarea>
              {value.length}/{maxLength}
            </CurrentLengthOfTextarea>
          )}
        </Box>
      )}
      <Space size={0} />
      <ErrorText>{errorMessage}</ErrorText>
    </>
  );
};
const ErrorText = styled.span`
  display: block;
  min-height: 20px;
  font-size: 14px;
  color: #ff4040;
`;

const CurrentLengthOfInput = styled.span`
  display: block;
  position: absolute;
  bottom: 4px;
  right: 4px;
  color: ${({ theme }) => theme.color.gray};
  font-size: ${({ theme }) => theme.fontSize.extraSmall};
`;

const CurrentLengthOfTextarea = styled.span`
  display: block;
  position: absolute;
  bottom: 8px;
  right: 4px;
  color: ${({ theme }) => theme.color.gray};
  font-size: ${({ theme }) => theme.fontSize.extraSmall};
`;

export default InputContainer;

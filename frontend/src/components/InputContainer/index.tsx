import { styled } from 'styled-components';
import Flex from '../common/Flex';
import Input from '../common/Input';
import Space from '../common/Space';
import Text from '../common/Text';
import Textarea from '../common/Textarea';
import { ForwardedRef, forwardRef } from 'react';

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
  ) => void;
  tabIndex: number;
  errorMessage: string;
  readOnly?: boolean;
  autoFocus?: boolean;
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
}: InputContainerProps) => {
  const onChangeUserInput = (
    e: React.ChangeEvent<HTMLInputElement | HTMLTextAreaElement>,
  ) => {
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
        <Input
          name={name}
          value={value}
          placeholder={placeholder}
          onChange={onChangeUserInput}
          tabIndex={tabIndex}
          $hasBadWord={errorMessage.length > 0}
          readOnly={readOnly}
          autoFocus={autoFocus}
        />
      ) : (
        <Textarea
          name={name}
          value={value}
          placeholder={placeholder}
          onChange={onChangeUserInput}
          tabIndex={tabIndex}
          $hasBadWord={errorMessage.length > 0}
        />
      )}
      <Space size={0} />
      <ErrorText>{errorMessage}</ErrorText>
    </>
  );
};
const ErrorText = styled.span`
  display: block;
  height: 20px;
  font-size: 14px;
  color: #ff4040;
`;

export default InputContainer;

import { styled } from 'styled-components';
import Flex from '../Flex';
import Spacing from '../Space';
import Text from '../Text';

export interface TopicCardProps {
  pinTitle: string;
  pinLocation: string;
  pinInformation: string;
}

const PinPreview = ({
  pinTitle,
  pinLocation,
  pinInformation,
}: TopicCardProps) => {
  return (
    <Flex
      width="360px"
      height="150px"
      position="relative"
      $flexDirection="column"
      $backgroundColor="white"
      $borderBottom="1px solid #E7E7E7"
    >
      <PinButton></PinButton>
      <Text color="black" $fontSize="default" $fontWeight="bold">
        {pinTitle}
      </Text>
      <Text color="gray" $fontSize="small" $fontWeight="normal">
        {pinLocation}
      </Text>
      <Spacing size={3} />
      <Text color="black" $fontSize="small" $fontWeight="normal">
        {pinInformation}
      </Text>
    </Flex>
  );
};

const PinButton = styled.button`
  width: 16px;
  height: 16px;

  position: absolute;
  top: 8px;
  right: 8px;

  background-color: ${({ theme }) => theme.color.white};
  border: 1px solid ${({ theme }) => theme.color.black};
  border-radius: ${({ theme }) => theme.radius.medium};

  &:focus {
    background-color: ${({ theme }) => theme.color.primary};
  }
`;

export default PinPreview;

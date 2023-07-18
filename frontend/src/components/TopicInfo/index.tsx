import { styled } from 'styled-components';
import Flex from '../common/Flex';
import Text from '../common/Text';
import Clipping from '../../assets/clipping.svg';
import Share from '../../assets/share.svg';
import Button from '../common/Button';
import Space from '../common/Space';

export interface TopicCardProps {
  topicParticipant: string;
  pinNumber: string;
  topicTitle: string;
  topicOwner: string;
  topicDescription: string;
}

const TopicInfo = ({
  topicParticipant,
  pinNumber,
  topicTitle,
  topicOwner,
  topicDescription,
}: TopicCardProps) => {
  return (
    <Flex
      width="360px"
      height="195px"
      position="relative"
      $flexDirection="column"
      $backgroundColor="white"
      $borderBottom="1px solid #E7E7E7"
    >
      <Flex>
        <Text color="gray" $fontSize="small" $fontWeight="normal">
          {topicParticipant}
        </Text>
        <Space size={2} />
        <Text color="gray" $fontSize="small" $fontWeight="normal">
          {pinNumber}
        </Text>
      </Flex>
      <Text color="black" $fontSize="large" $fontWeight="bold">
        {topicTitle}
      </Text>
      <Text color="black" $fontSize="small" $fontWeight="normal">
        {topicOwner}
      </Text>
      <Text color="gray" $fontSize="small" $fontWeight="normal">
        {topicDescription}
      </Text>
      <Space size={2} />
      <Flex $justifyContent="space-between">
        <Flex>
          <Clipping />
          <Space size={2} />
          <Share />
        </Flex>
        <Button variant="secondary">+ 핀 추가하기</Button>
      </Flex>
      <Space size={6} />
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

export default TopicInfo;

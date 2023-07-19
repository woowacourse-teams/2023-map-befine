import { styled } from 'styled-components';
import Flex from '../common/Flex';
import Text from '../common/Text';
import { useNavigate } from 'react-router-dom';

export interface TopicCardProps {
  topicId: string;
  topicEmoji: string;
  topicTitle: string;
  topicInformation: string;
}

const TopicCard = ({
  topicId,
  topicEmoji,
  topicTitle,
  topicInformation,
}: TopicCardProps) => {
  const navigator = useNavigate();

  const goToSelectedTopic = () => {
    navigator(`topics/${topicId}`);
  };

  return (
    <Flex
      width="360px"
      height="140px"
      position="relative"
      $flexDirection="column"
      $alignItems="center"
      $justifyContent="center"
      $backgroundColor="whiteGray"
      $borderRadius="small"
    >
      <MultiSelectButton type="checkbox"></MultiSelectButton>
      <Flex
        $flexDirection="column"
        $alignItems="center"
        $justifyContent="center"
        cursor="pointer"
        onClick={goToSelectedTopic}
      >
        <Text color="black" $fontSize="extraLarge" $fontWeight="normal">
          {topicEmoji}
        </Text>
        <Text color="black" $fontSize="medium" $fontWeight="normal">
          {topicTitle}
        </Text>
        <Text color="gray" $fontSize="small" $fontWeight="normal">
          {topicInformation}
        </Text>
      </Flex>
    </Flex>
  );
};

const MultiSelectButton = styled.input`
  width: 24px;
  height: 24px;
  position: absolute;
  top: 8px;
  right: 8px;
  background-color: ${({ theme }) => theme.color.white};
  border: 1px solid ${({ theme }) => theme.color.black};
  border-radius: ${({ theme }) => theme.radius.small};
  cursor: pointer;

  &:focus {
    background-color: ${({ theme }) => theme.color.primary};
  }
`;

export default TopicCard;

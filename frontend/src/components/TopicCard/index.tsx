import { styled } from 'styled-components';
import Flex from '../common/Flex';
import Text from '../common/Text';
import useNavigator from '../../hooks/useNavigator';
import Box from '../common/Box';

export interface TopicCardProps {
  topicId: number;
  topicImage: string;
  topicTitle: string;
  topicUpdatedAt: string;
  topicPinCount: number;
  tagTopics: string[];
  setTagTopics: React.Dispatch<React.SetStateAction<string[]>>;
  taggedTopicIds: number[];
  setTaggedTopicIds: React.Dispatch<React.SetStateAction<number[]>>;
}

const TopicCard = ({
  topicId,
  topicImage,
  topicTitle,
  topicUpdatedAt,
  topicPinCount,
  tagTopics,
  setTagTopics,
  taggedTopicIds,
  setTaggedTopicIds,
}: TopicCardProps) => {
  const { routePage } = useNavigator();
  const goToSelectedTopic = () => {
    routePage(`topics/${topicId}`, [topicId]);
  };

  const onAddTagOfTopic = (e: React.ChangeEvent<HTMLInputElement>) => {
    if (e.target.checked) {
      setTagTopics([...tagTopics, topicTitle]);
      setTaggedTopicIds((prev) => [...prev, topicId]);
    } else {
      setTagTopics(tagTopics.filter((value) => value !== topicTitle));
      setTaggedTopicIds(taggedTopicIds.filter((value) => value !== topicId));
    }
  };

  return (
    <WrapperLi>
      <Flex
        width="360px"
        height="140px"
        position="relative"
        $flexDirection="column"
        $alignItems="center"
        $justifyContent="center"
        $borderRadius="small"
        $backgroundImage={topicImage}
        $backgroundSize="360px 140px"
        $backgroundColor={'whiteGray'}
      >
        <MultiSelectButton
          type="checkbox"
          onChange={(e: React.ChangeEvent<HTMLInputElement>) =>
            onAddTagOfTopic(e)
          }
          checked={taggedTopicIds.includes(topicId)}
        />
        <Box
          position="absolute"
          width="100%"
          height="68px"
          $backgroundColor="black"
          opacity="0.6"
        ></Box>
        <Flex
          width="100%"
          height="68px"
          $flexDirection="column"
          $alignItems="center"
          $justifyContent="center"
          cursor="pointer"
          onClick={goToSelectedTopic}
          $backdropFilter="blur(20px)"
        >
          <Text color="white" $fontSize="medium" $fontWeight="normal">
            {topicTitle}
          </Text>
          <Text color="white" $fontSize="small" $fontWeight="normal">
            {`업데이트 : ${
              topicUpdatedAt.split('T')[0]
            } | 핀 개수 : ${topicPinCount}`}
          </Text>
        </Flex>
      </Flex>
    </WrapperLi>
  );
};

const WrapperLi = styled.li`
  box-shadow: 2px 4px 4px 0px rgba(69, 69, 69, 0.25);
`;

const MultiSelectButton = styled.input`
  width: 24px;
  height: 24px;
  position: absolute;
  top: ${({ theme }) => theme.spacing[0]};
  right: ${({ theme }) => theme.spacing[0]};
  background-color: ${({ theme }) => theme.color.white};
  border: 1px solid ${({ theme }) => theme.color.black};
  border-radius: ${({ theme }) => theme.radius.small};
  cursor: pointer;

  &:focus {
    background-color: ${({ theme }) => theme.color.primary};
  }
`;

export default TopicCard;

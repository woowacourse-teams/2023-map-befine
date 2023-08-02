import { styled } from 'styled-components';
import Flex from '../common/Flex';
import Text from '../common/Text';
import useNavigator from '../../hooks/useNavigator';
import { useContext } from 'react';
import { TagIdContext } from '../../store/TagId';

export interface TopicCardProps {
  topicId: number;
  topicImage: string;
  topicTitle: string;
  topicUpdatedAt: string;
  topicPinCount: number;
  tagTopics: string[];
  setTagTopics: (value: string[]) => void;
}

const TopicCard = ({
  topicId,
  topicImage,
  topicTitle,
  topicUpdatedAt,
  topicPinCount,
  tagTopics,
  setTagTopics,
}: TopicCardProps) => {
  const { routePage } = useNavigator();

  const { tagId, setTagId } = useContext(TagIdContext);

  const goToSelectedTopic = () => {
    routePage(`topics/${topicId}`, [topicId]);
  };

  const onAddTagOfTopic = (e: React.ChangeEvent<HTMLInputElement>) => {
    if (e.target.checked) {
      setTagTopics([...tagTopics, topicTitle]);
      setTagId([...tagId, topicId]);
    } else {
      setTagTopics(tagTopics.filter((value) => value !== topicTitle));
      setTagId(tagId.filter((value) => value !== topicId));
    }
  };

  return (
    <li>
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
      >
        <MultiSelectButton
          type="checkbox"
          onChange={(e: React.ChangeEvent<HTMLInputElement>) =>
            onAddTagOfTopic(e)
          }
          checked={Boolean(tagId.includes(topicId))}
        />
        <Flex
          width="100%"
          height="65px"
          $flexDirection="column"
          $alignItems="center"
          $justifyContent="center"
          cursor="pointer"
          onClick={goToSelectedTopic}
          $backdropFilter="blur(20px)"
        >
          <Text color="black" $fontSize="medium" $fontWeight="normal">
            {topicTitle}
          </Text>
          <Text color="gray" $fontSize="small" $fontWeight="normal">
            {`업데이트 : ${topicUpdatedAt.split('T')[0]} | 핀 개수 : ${topicPinCount}`}
          </Text>
        </Flex>
      </Flex>
    </li>
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

import { styled } from 'styled-components';
import Text from '../common/Text';
import useNavigator from '../../hooks/useNavigator';
import Box from '../common/Box';
import Image from '../common/Image';
import { SyntheticEvent, useContext } from 'react';
import Space from '../common/Space';
import Flex from '../common/Flex';
import SmallTopicPin from '../../assets/smallTopicPin.svg';
import SmallTopicStar from '../../assets/smallTopicStar.svg';
import { DEFAULT_TOPIC_IMAGE } from '../../constants';
import { ModalContext } from '../../context/ModalContext';

const FAVORITE_COUNT = 10;

export interface ModalTopicCardProps {
  topicId: number;
  topicImage: string;
  topicTitle: string;
  topicUpdatedAt: string;
  topicPinCount: number;
  topicClick: any;
  topicBookmarkCount: number;
}

const ModalTopicCard = ({
  topicId,
  topicImage,
  topicTitle,
  topicUpdatedAt,
  topicPinCount,
  topicClick,
  topicBookmarkCount,
}: ModalTopicCardProps) => {
  const { routePage } = useNavigator();
  const { closeModal } = useContext(ModalContext);
  const goToSelectedTopic = (topic: any, type: 'newPin' | 'addToTopic') => {
    if (type === 'newPin') {
      topicClick(topic);
      closeModal('newPin');
    }
    if (type === 'addToTopic') {
      topicClick(topic);
    }
  };

  return (
    <Wrapper
      onClick={() => {
        goToSelectedTopic({ topicId, topicTitle }, 'newPin');
      }}
    >
      <Flex position="relative">
        <TopicImage
          height="138px"
          width="138px"
          src={topicImage}
          alt="토픽 이미지"
          $objectFit="cover"
          onError={(e: SyntheticEvent<HTMLImageElement, Event>) => {
            e.currentTarget.src = DEFAULT_TOPIC_IMAGE;
          }}
        />

        <Flex
          $alignItems="flex-start"
          $flexDirection="column"
          width="192px"
          padding={1}
        >
          <Box height="52px">
            <Text color="black" $fontSize="default" $fontWeight="bold">
              {topicTitle}
            </Text>
          </Box>

          <Text color="black" $fontSize="small" $fontWeight="normal">
            토픽 생성자
          </Text>

          <Space size={0} />

          <Text color="gray" $fontSize="small" $fontWeight="normal">
            {topicUpdatedAt.split('T')[0].replaceAll('-', '.')} 업데이트
          </Text>

          <Space size={0} />

          <Flex>
            <Flex $alignItems="center" width="64px">
              <SmallTopicPin />
              <Space size={0} />
              <Text color="black" $fontSize="extraSmall" $fontWeight="normal">
                {topicPinCount > 999 ? '+999' : topicPinCount}개
              </Text>
            </Flex>
            <Flex $alignItems="center" width="64px">
              <SmallTopicStar />
              <Space size={0} />
              <Text color="black" $fontSize="extraSmall" $fontWeight="normal">
                {topicBookmarkCount > 999 ? '+999' : topicBookmarkCount}명
              </Text>
            </Flex>
          </Flex>
        </Flex>
      </Flex>
    </Wrapper>
  );
};

const Wrapper = styled.li`
  width: 332px;
  height: 140px;
  cursor: pointer;
  border: 1px solid ${({ theme }) => theme.color.gray};
  border-radius: ${({ theme }) => theme.radius.small};
`;

const TopicImage = styled(Image)`
  border-radius: ${({ theme }) => theme.radius.small};
`;

export default ModalTopicCard;

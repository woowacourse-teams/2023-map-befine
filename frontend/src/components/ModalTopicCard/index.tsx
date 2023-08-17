import { styled } from 'styled-components';
import Text from '../common/Text';
import useNavigator from '../../hooks/useNavigator';
import Box from '../common/Box';
import Image from '../common/Image';
import { SyntheticEvent } from 'react';
import Space from '../common/Space';
import Flex from '../common/Flex';
import SmallTopicPin from '../../assets/smallTopicPin.svg';
import SmallTopicStar from '../../assets/smallTopicStar.svg';
import { DEFAULT_TOPIC_IMAGE } from '../../constants';

const FAVORITE_COUNT = 10;

export interface ModalTopicCardProps {
  topicId: number;
  topicImage: string;
  topicTitle: string;
  topicUpdatedAt: string;
  topicPinCount: number;
}

const ModalTopicCard = ({
  topicId,
  topicImage,
  topicTitle,
  topicUpdatedAt,
  topicPinCount,
}: ModalTopicCardProps) => {
  const { routePage } = useNavigator();

  const goToSelectedTopic = () => {
    routePage(`/topics/${topicId}`, [topicId]);
  };

  return (
    <Wrapper onClick={goToSelectedTopic}>
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

        <Box width="192px" padding={1}>
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
                {FAVORITE_COUNT > 999 ? '+999' : FAVORITE_COUNT}명
              </Text>
            </Flex>
          </Flex>
        </Box>
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

  margin: 0 auto;
`;

const ButtonWrapper = styled.div`
  display: flex;
  justify-content: space-between;
  position: absolute;
  width: 72px;

  top: 100px;
  left: 60px;
`;

const TopicImage = styled(Image)`
  border-radius: ${({ theme }) => theme.radius.small};
`;

export default ModalTopicCard;

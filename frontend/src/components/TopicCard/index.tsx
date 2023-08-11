import { styled } from 'styled-components';
import Text from '../common/Text';
import useNavigator from '../../hooks/useNavigator';
import Box from '../common/Box';
import Image from '../common/Image';
import { SyntheticEvent } from 'react';
import Space from '../common/Space';
import Flex from '../common/Flex';
import SeeTogetherButton from '../SeeTogetherButton';
import SmallTopicPin from '../../assets/smallTopicPin.svg';
import SmallTopicStar from '../../assets/smallTopicStar.svg';

const FAVORITE_COUNT = 10;

export interface TopicCardProps {
  topicId: number;
  topicImage: string;
  topicTitle: string;
  topicUpdatedAt: string;
  topicPinCount: number;
}

const TopicCard = ({
  topicId,
  topicImage,
  topicTitle,
  topicUpdatedAt,
  topicPinCount,
}: TopicCardProps) => {
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
            e.currentTarget.src =
              'https://velog.velcdn.com/images/semnil5202/post/37dae18f-9860-4483-bad5-1158a210e5a8/image.svg';
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

          <Flex $alignItems="center">
            <Flex width="68px">
              <SmallTopicPin />
              <Space size={0} />
              <Text color="black" $fontSize="extraSmall" $fontWeight="normal">
                {topicPinCount > 999 ? '+999' : topicPinCount}개
              </Text>
            </Flex>

            <Space size={1} />

            <Flex width="68px">
              <SmallTopicStar />
              <Space size={0} />
              <Text color="black" $fontSize="extraSmall" $fontWeight="normal">
                {FAVORITE_COUNT > 999 ? '+999' : FAVORITE_COUNT}명
              </Text>
            </Flex>
          </Flex>

          <ButtonWrapper>
            <SeeTogetherButton />
          </ButtonWrapper>
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
`;

const ButtonWrapper = styled.div`
  position: absolute;
  top: 100px;
  left: 100px;
`;

const TopicImage = styled(Image)`
  border-radius: ${({ theme }) => theme.radius.small};
`;

export default TopicCard;

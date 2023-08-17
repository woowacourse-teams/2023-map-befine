import { styled } from 'styled-components';
import Text from '../common/Text';
import useNavigator from '../../hooks/useNavigator';
import Box from '../common/Box';
import Image from '../common/Image';
import { SyntheticEvent } from 'react';
import Space from '../common/Space';
import Flex from '../common/Flex';
import FavoriteSVG from '../../assets/favoriteBtn_filled.svg';
import SeeTogetherSVG from '../../assets/seeTogetherBtn_filled.svg';
import SmallTopicPin from '../../assets/smallTopicPin.svg';
import SmallTopicStar from '../../assets/smallTopicStar.svg';
import { DEFAULT_TOPIC_IMAGE } from '../../constants';
import AddSeeTogether from '../AddSeeTogether';
import AddFavorite from '../AddFavorite';

export interface PinCardProps {
  pinId: number;
  pinTitle: string;
  pinAddress: string;
  pinDescription: string;
}

const PinCard = ({
  pinId,
  pinTitle,
  pinAddress,
  pinDescription,
}: PinCardProps) => {
  const { routePage } = useNavigator();

  return (
    <Wrapper>
      <Flex position="relative">
        <Box width="192px" padding={1}>
          <Box height="52px">
            <Text color="black" $fontSize="default" $fontWeight="bold">
              {pinTitle}
            </Text>
          </Box>

          <Space size={0} />

          <Text color="gray" $fontSize="small" $fontWeight="normal">
            {pinAddress}
          </Text>

          <Space size={0} />
          <Text color="gray" $fontSize="small" $fontWeight="normal">
            {pinDescription}
          </Text>
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

export default PinCard;

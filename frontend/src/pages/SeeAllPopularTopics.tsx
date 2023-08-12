import { useLocation } from 'react-router-dom';
import SeeAllCardList from '../components/SeeAllCardList';
import Text from '../components/common/Text';
import Space from '../components/common/Space';
import { styled } from 'styled-components';
import Box from '../components/common/Box';
import useSetLayoutWidth from '../hooks/useSetLayoutWidth';
import { FULLSCREEN } from '../constants';

const url = '/topics';

const SeeAllTopics = () => {
  const { width: _ } = useSetLayoutWidth(FULLSCREEN);

  return (
    <Wrapper>
      <Text color="black" $fontSize="extraLarge" $fontWeight="bold">
        인기 급상승할 지도?
      </Text>

      <Space size={4} />

      <SeeAllCardList url={url} />
    </Wrapper>
  );
};

const Wrapper = styled(Box)`
  width: 1036px;
  margin: 0 auto;
`;

export default SeeAllTopics;

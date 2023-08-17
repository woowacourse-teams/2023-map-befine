import { styled } from 'styled-components';
import SeeAllCardList from '../components/SeeAllCardList';
import Space from '../components/common/Space';
import Text from '../components/common/Text';
import { FULLSCREEN } from '../constants';
import useSetLayoutWidth from '../hooks/useSetLayoutWidth';
import useSetNavbarHighlight from '../hooks/useSetNavbarHighlight';
import Box from '../components/common/Box';

const url = '/topics/newest';

const SeeAllLatestTopics = () => {
  const { width: _ } = useSetLayoutWidth(FULLSCREEN);
  const { navbarHighlights: __ } = useSetNavbarHighlight('home');

  return (
    <Wrapper>
      <Space size={5} />
      <Text color="black" $fontSize="extraLarge" $fontWeight="bold">
        내 주변일 지도?
      </Text>

      <Space size={5} />

      <SeeAllCardList url={url} />
    </Wrapper>
  );
};

const Wrapper = styled(Box)`
  width: 1036px;
  margin: 0 auto;
`;

export default SeeAllLatestTopics;

import { useLocation } from 'react-router-dom';
import SeeAllCardList from '../components/SeeAllCardList';
import Text from '../components/common/Text';
import Space from '../components/common/Space';
import { styled } from 'styled-components';
import Box from '../components/common/Box';

const SeeAllTopics = () => {
  const { state } = useLocation();
  const url = state.split('|')[0];
  const title = state.split('|')[1];

  return (
    <Wrapper>
      <Text color="black" $fontSize="extraLarge" $fontWeight="bold">
        {title}
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

import { useLocation } from 'react-router-dom';
import SeeAllCardList from '../components/SeeAllCardList';
import Text from '../components/common/Text';
import Space from '../components/common/Space';

const SeeAllTopics = () => {
  const { state } = useLocation();
  const url = state.split('|')[0];
  const title = state.split('|')[1];

  return (
    <>
      <Text color="black" $fontSize="large" $fontWeight="bold">
        {title}
      </Text>
      <SeeAllCardList url={url} />
    </>
  );
};

export default SeeAllTopics;

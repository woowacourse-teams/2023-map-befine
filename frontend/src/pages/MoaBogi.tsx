import { useLocation } from 'react-router-dom';
import Text from '../components/common/Text';
import SeeAllCardList from '../components/SeeAllCardList';

const MoaBogi = () => {
  const { state } = useLocation();
  const url = state.split('|')[0];
  const title = state.split('|')[1];
  // routePage(`/new-pin?topic-id=${topicId}`, fullUrl);
  return (
    <>
      <Text color="black" $fontSize="large" $fontWeight="bold">
        {title}
      </Text>
      <SeeAllCardList url={url} />
    </>
  );
};

export default MoaBogi;

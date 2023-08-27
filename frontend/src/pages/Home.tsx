import Space from '../components/common/Space';
import Box from '../components/common/Box';
import useNavigator from '../hooks/useNavigator';
import TopicListContainer from '../components/TopicListContainer';
import { styled } from 'styled-components';
import useSetLayoutWidth from '../hooks/useSetLayoutWidth';
import { FULLSCREEN } from '../constants';
import useSetNavbarHighlight from '../hooks/useSetNavbarHighlight';
import { TopicCardProps } from '../types/Topic';
import { useEffect, useState } from 'react';
import Text from '../components/common/Text';
import useGet from '../apiHooks/useGet';

const Home = () => {
  const [topics, setTopics] = useState<
    Record<'popular' | 'near' | 'newest', TopicCardProps[] | null>
  >({
    popular: null,
    near: null,
    newest: null,
  });
  const { routePage } = useNavigator();
  const { fetchGet } = useGet();
  useSetLayoutWidth(FULLSCREEN);
  useSetNavbarHighlight('home');

  const goToPopularTopics = () => {
    routePage('see-all/popularity');
  };

  const goToNearByMeTopics = () => {
    routePage('see-all/near');
  };

  const goToLatestTopics = () => {
    routePage('see-all/latest');
  };

  const getNearTopicsFromServer = async () => {
    fetchGet<TopicCardProps[]>(
      '/topics',
      '내 주변 지도를 가져오는데 실패했습니다.',
      (response) => {
        setTopics((prevState) => ({ ...prevState, near: response }));
      },
    );
  };

  const getNewestTopicsFromServer = async () => {
    fetchGet<TopicCardProps[]>(
      '/topics/newest',
      '새롭게 장소가 추가된 지도를 가져오는데 실패했습니다.',
      (response) => {
        setTopics((prevState) => ({ ...prevState, newest: response }));
      },
    );
  };

  const getPopularTopicsFromServer = async () => {
    fetchGet<TopicCardProps[]>(
      '/topics/bests',
      '즐겨찾기가 많이 된 지도를 가져오는데 실패했습니다.',
      (response) => {
        setTopics((prevState) => ({ ...prevState, popular: response }));
      },
    );
  };

  const topicsFetchingFromServer = async () => {
    await getPopularTopicsFromServer();
    await getNearTopicsFromServer();
    await getNewestTopicsFromServer();
  };

  useEffect(() => {
    topicsFetchingFromServer();
  }, []);

  const { popular, near, newest } = topics;

  if (!(popular && near && newest)) return <></>;

  if (popular.length === 0 && near.length === 0 && newest.length === 0) {
    return (
      <EmptyWrapper>
        <Text color="primary" $fontSize="extraLarge" $fontWeight="bold">
          추가하기 버튼을 눌러 지도를 추가해보세요!
        </Text>
        <Space size={1} />
        <Text color="black" $fontSize="default" $fontWeight="normal">
          지도가 없습니다.
        </Text>
      </EmptyWrapper>
    );
  }

  return (
    <>
      <Wrapper position="relative">
        <Space size={5} />
        <TopicListContainer
          containerTitle="인기 급상승할 지도?"
          containerDescription="즐겨찾기가 많이 된 지도를 확인해보세요."
          routeWhenSeeAll={goToPopularTopics}
          topics={popular}
          setTopicsFromServer={topicsFetchingFromServer}
        />
        <Space size={9} />
        <TopicListContainer
          containerTitle="새로울 지도?"
          containerDescription="방금 새로운 핀이 추가된 지도를 확인해보세요."
          routeWhenSeeAll={goToLatestTopics}
          topics={newest}
          setTopicsFromServer={topicsFetchingFromServer}
        />
        <Space size={9} />
        <TopicListContainer
          containerTitle="모두일 지도?"
          containerDescription="괜찮을지도의 모든 지도를 확인해보세요."
          routeWhenSeeAll={goToNearByMeTopics}
          topics={near}
          setTopicsFromServer={topicsFetchingFromServer}
        />
        <Space size={5} />
      </Wrapper>
    </>
  );
};

const EmptyWrapper = styled.section`
  display: flex;
  flex-direction: column;
  justify-content: center;
  align-items: center;
  width: 1036px;
  height: 100vh;
  margin: 0 auto;
`;

const Wrapper = styled(Box)`
  width: 1036px;
  margin: 0 auto;
`;

const ModalWrapper = styled.div`
  width: 300px;
  height: 300px;
  background-color: white;
`;

export default Home;

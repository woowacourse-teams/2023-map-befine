import Space from '../components/common/Space';
import Box from '../components/common/Box';
import useNavigator from '../hooks/useNavigator';
import TopicListContainer from '../components/TopicListContainer';
import { styled } from 'styled-components';
import useSetLayoutWidth from '../hooks/useSetLayoutWidth';
import { FULLSCREEN } from '../constants';
import useSetNavbarHighlight from '../hooks/useSetNavbarHighlight';
import useToast from '../hooks/useToast';
import { TopicType } from '../types/Topic';
import { getApi } from '../apis/getApi';
import { useEffect, useState } from 'react';

const Home = () => {
  const [popularTopics, setPopularTopics] = useState<TopicType[] | null>(null);
  const [nearTopics, setNearTopics] = useState<TopicType[] | null>(null);
  const [newestTopics, setNewestTopics] = useState<TopicType[] | null>(null);
  const { routePage } = useNavigator();
  const { width: _ } = useSetLayoutWidth(FULLSCREEN);
  const { navbarHighlights: __ } = useSetNavbarHighlight('home');
  const { showToast } = useToast();

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
    try {
      const topics = await getApi<TopicType[]>(
        'default',
        `/locations/bests?latitude=37.0&longitude=127.0`,
      );

      if (topics.length === 0) {
        const topics = await getApi<TopicType[]>('default', `/topics`);
        setNearTopics(topics);
        return;
      }

      setNearTopics(topics);
    } catch {
      showToast(
        'error',
        '로그인 정보가 만료되었습니다. 로그아웃 후 다시 로그인 해주세요.',
      );
    }
  };

  const getNewestTopicsFromServer = async () => {
    try {
      const topics = await getApi<TopicType[]>('default', '/topics/newest');
      setNewestTopics(topics);
    } catch {
      showToast(
        'error',
        '로그인 정보가 만료되었습니다. 로그아웃 후 다시 로그인 해주세요.',
      );
    }
  };

  const getPopularTopicsFromServer = async () => {
    try {
      const topics = await getApi<TopicType[]>('default', '/topics/bests');
      setPopularTopics(topics);
    } catch {
      showToast(
        'error',
        '로그인 정보가 만료되었습니다. 로그아웃 후 다시 로그인 해주세요.',
      );
    }
  };

  const topicsFetchingFromServer = async () => {
    await getPopularTopicsFromServer();
    await getNearTopicsFromServer();
    await getNewestTopicsFromServer();
  };

  useEffect(() => {
    topicsFetchingFromServer();
  }, []);

  if (!(popularTopics && nearTopics && newestTopics)) return <></>;

  return (
    <>
      <Wrapper position="relative">
        <Space size={5} />
        <TopicListContainer
          containerTitle="인기 급상승할 지도?"
          containerDescription="즐겨찾기가 많이 된 지도를 확인해보세요."
          routeWhenSeeAll={goToPopularTopics}
          topics={popularTopics}
          setTopicsFromServer={topicsFetchingFromServer}
        />
        <Space size={9} />
        <TopicListContainer
          containerTitle="내 주변일 지도?"
          containerDescription="내 주변에 있는 지도를 확인해보세요."
          routeWhenSeeAll={goToNearByMeTopics}
          topics={nearTopics}
          setTopicsFromServer={topicsFetchingFromServer}
        />
        <Space size={9} />
        <TopicListContainer
          containerTitle="새로울 지도?"
          containerDescription="방금 새로운 핀이 추가된 지도를 확인해보세요."
          routeWhenSeeAll={goToLatestTopics}
          topics={newestTopics}
          setTopicsFromServer={topicsFetchingFromServer}
        />
      </Wrapper>
    </>
  );
};

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

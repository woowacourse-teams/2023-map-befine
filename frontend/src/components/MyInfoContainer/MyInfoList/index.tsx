import { Fragment, useEffect, useState } from 'react';
import { styled } from 'styled-components';
import { getApi } from '../../../apis/getApi';
import PinCard from '../../PinCard';
import TopicCard from '../../TopicCard';
import { TopicType } from '../../../types/Topic';
import useToast from '../../../hooks/useToast';

const MyInfoList = () => {
  const [myInfoTopics, setMyInfoTopics] = useState<TopicType[]>([]);
  const { showToast } = useToast();

  const getMyInfoListFromServer = async () => {
    try {
      const serverMyInfoTopics = await getApi<TopicType[]>(
        'default',
        '/members/my/topics',
      );

      setMyInfoTopics(serverMyInfoTopics);
    } catch {
      showToast('error', '로그인 후 이용해주세요.');
    }
  };

  useEffect(() => {
    getMyInfoListFromServer();
  }, []);

  if (!myInfoTopics) return <></>;

  return (
    <MyInfoListWrapper>
      {myInfoTopics.map((topic, index) => {
        return (
          <Fragment key={topic.id}>
            <TopicCard
              id={topic.id}
              image={topic.image}
              name={topic.name}
              creator={topic.creator}
              updatedAt={topic.updatedAt}
              pinCount={topic.pinCount}
              bookmarkCount={topic.bookmarkCount}
              isInAtlas={topic.isInAtlas}
              isBookmarked={topic.isBookmarked}
              setTopicsFromServer={getMyInfoListFromServer}
            />
          </Fragment>
        );
      })}
    </MyInfoListWrapper>
  );
};

const MyInfoListWrapper = styled.ul`
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(340px, 1fr));
  grid-row-gap: ${({ theme }) => theme.spacing[5]};
`;

export default MyInfoList;

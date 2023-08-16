import { Fragment, useEffect, useState } from 'react';
import { styled } from 'styled-components';
import { getApi } from '../../../apis/getApi';
import { MyInfoPinType, MyInfoTopicType } from '../../../types/MyInfo';
import PinCard from '../../PinCard';
import TopicCard from '../../TopicCard';

const MyInfoList = () => {
  const [myInfoTopics, setMyInfoTopics] = useState<MyInfoTopicType[]>([]);

  const getMyInfoListFromServer = async () => {
    const serverMyInfoTopics = await getApi<MyInfoTopicType[]>(
      'default',
      '/members/my/topics',
    );
    setMyInfoTopics(serverMyInfoTopics);
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
              topicId={topic.id}
              topicImage={topic.image}
              topicTitle={topic.name}
              topicUpdatedAt={topic.updatedAt}
              topicPinCount={topic.pinCount}
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

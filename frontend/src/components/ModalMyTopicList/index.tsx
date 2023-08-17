import { Fragment, useEffect, useState } from 'react';
import { styled } from 'styled-components';
import { getApi } from '../../apis/getApi';
import { ModalMyTopicType } from '../../types/Topic';
import ModalTopicCard from '../ModalTopicCard';

const ModalMyTopicList = ({ topicClick }: any) => {
  const [myTopics, setMyTopics] = useState<ModalMyTopicType[]>([]);
  const getMyTopicFromServer = async () => {
    const serverMyTopic = await getApi<ModalMyTopicType[]>(
      'default',
      '/members/my/topics',
    );
    setMyTopics(serverMyTopic);
  };

  useEffect(() => {
    getMyTopicFromServer();
  }, []);
  {
  }

  if (!myTopics) return <></>;

  return (
    <ModalMyTopicListWrapper>
      {myTopics.map((topic) => (
        <Fragment key={topic.id}>
          <ModalTopicCard
            topicId={topic.id}
            topicImage={topic.image}
            topicTitle={topic.name}
            topicUpdatedAt={topic.updatedAt}
            topicPinCount={topic.pinCount}
            topicClick={topicClick}
          />
        </Fragment>
      ))}
    </ModalMyTopicListWrapper>
  );
};

const ModalMyTopicListWrapper = styled.ul`
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(340px, 1fr));
  grid-row-gap: ${({ theme }) => theme.spacing[5]};
`;

export default ModalMyTopicList;

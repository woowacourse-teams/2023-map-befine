import React, { Fragment, useEffect, useState } from 'react';
import { styled } from 'styled-components';
import { getApi } from '../../apis/getApi';
import { ModalMyTopicType } from '../../types/Topic';
import ModalTopicCard from '../ModalTopicCard';
import Space from '../common/Space';

interface ModalMyTopicList {
  topicId: string;
  topicClick: any;
}

const ModalMyTopicList = ({ topicId, topicClick }: ModalMyTopicList) => {
  const [myTopics, setMyTopics] = useState<ModalMyTopicType[]>([]);

  const getMyTopicFromServer = async () => {
    if (topicId && topicId.split(',').length > 1) {
      const topics = await getApi<ModalMyTopicType[]>(
        `/topics/ids?ids=${topicId}`,
      );

      setMyTopics(topics);
      return;
    }
    const serverMyTopic = await getApi<ModalMyTopicType[]>(
      '/members/my/topics',
    );
    setMyTopics(serverMyTopic);
  };

  useEffect(() => {
    getMyTopicFromServer();
  }, []);

  if (!myTopics) return <></>;

  return (
    <>
      <ModalMyTopicListWrapper>
        {myTopics.map((topic) => (
          <Fragment key={topic.id}>
            <ModalTopicCard
              topicId={topic.id}
              topicImage={topic.image}
              topicTitle={topic.name}
              topicCreator={topic.creator}
              topicUpdatedAt={topic.updatedAt}
              topicPinCount={topic.pinCount}
              topicClick={topicClick}
              topicBookmarkCount={topic.bookmarkCount}
            />
          </Fragment>
        ))}
      </ModalMyTopicListWrapper>
      <Space size={5} />
    </>
  );
};

const ModalMyTopicListWrapper = styled.ul`
  width: 684px;
  margin: 0 auto;
  display: flex;
  flex-wrap: wrap;
  gap: 20px;
`;

export default ModalMyTopicList;

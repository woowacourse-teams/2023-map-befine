import { Fragment, useEffect, useState } from 'react';
import { styled } from 'styled-components';

import useGet from '../../apiHooks/useGet';
import { TopicCardProps } from '../../types/Topic';
import Space from '../common/Space';
import TopicCard from '../TopicCard';

interface ModalMyTopicListProps {
  topicId: string;
  topicClick: any;
}

function ModalMyTopicList({ topicId, topicClick }: ModalMyTopicListProps) {
  const [myTopics, setMyTopics] = useState<TopicCardProps[] | null>(null);
  const { fetchGet } = useGet();

  const getMyTopicFromServer = async () => {
    if (topicId && topicId.split(',').length > 1) {
      fetchGet<TopicCardProps[]>(
        `/topics/ids?ids=${topicId}`,
        '모아보기로 선택한 지도 목록을 조회하는데 실패했습니다.',
        (response) => {
          setMyTopics(response);
        },
      );

      return;
    }

    fetchGet<TopicCardProps[]>(
      '/members/my/topics',
      '나의 지도 목록을 조회하는데 실패했습니다.',
      (response) => {
        setMyTopics(response);
      },
    );
  };

  useEffect(() => {
    getMyTopicFromServer();
  }, []);

  if (!myTopics) return null;

  return (
    <>
      <ModalMyTopicListWrapper>
        {myTopics.map((topic) => (
          <Fragment key={topic.id}>
            <TopicCard
              cardType="modal"
              id={topic.id}
              image={topic.image}
              name={topic.name}
              creator={topic.creator}
              updatedAt={topic.updatedAt}
              pinCount={topic.pinCount}
              onClickDesignated={topicClick}
              bookmarkCount={topic.bookmarkCount}
              isBookmarked={topic.isBookmarked}
              isInAtlas={topic.isInAtlas}
            />
          </Fragment>
        ))}
      </ModalMyTopicListWrapper>
      <Space size={5} />
    </>
  );
}

const ModalMyTopicListWrapper = styled.ul`
  width: 684px;
  margin: 0 auto;
  display: flex;
  flex-wrap: wrap;
  gap: 20px;

  @media (max-width: 744px) {
    width: 100%;
    justify-content: center;
    margin-bottom: 48px;
  }
`;

export default ModalMyTopicList;

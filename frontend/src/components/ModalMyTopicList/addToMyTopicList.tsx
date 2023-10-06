import { Fragment, useContext, useEffect, useState } from 'react';
import { styled } from 'styled-components';

import useGet from '../../apiHooks/useGet';
import usePost from '../../apiHooks/usePost';
import { ModalContext } from '../../context/ModalContext';
import useToast from '../../hooks/useToast';
import { TopicCardProps } from '../../types/Topic';
import Space from '../common/Space';
import TopicCard from '../TopicCard';

interface OnClickDesignatedProps {
  topicId: number;
  topicName: string;
}

function AddToMyTopicList({ pin }: any) {
  const [myTopics, setMyTopics] = useState<TopicCardProps[] | null>(null);
  const { closeModal } = useContext(ModalContext);
  const { fetchGet } = useGet();
  const { fetchPost } = usePost();
  const { showToast } = useToast();

  const getMyTopicsFromServer = async () => {
    fetchGet<TopicCardProps[]>(
      '/members/my/topics',
      '내가 만든 지도를 가져오는데 실패했습니다. 잠시 후 다시 시도해주세요.',
      (response) => {
        setMyTopics(response);
      },
    );
  };

  useEffect(() => {
    getMyTopicsFromServer();
  }, []);

  const addPinToTopic = async (topic: OnClickDesignatedProps) => {
    const url = `/topics/${topic.topicId}/copy?pinIds=${pin.id}`;

    fetchPost({
      url,
      errorMessage:
        '내 지도에 핀 추가를 실패하였습니다. 잠시 후 다시 시도해주세요.',
      onSuccess: () => {
        closeModal('addToMyTopicList');
        showToast('info', '내 지도에 핀이 추가되었습니다.');
      },
    });
  };

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
              onClickDesignated={addPinToTopic}
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

export default AddToMyTopicList;

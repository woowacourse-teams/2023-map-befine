import { Fragment, useContext, useEffect, useState } from 'react';
import { TopicCardProps } from '../../types/Topic';
import { styled } from 'styled-components';
import TopicCard from '../TopicCard';
import { ModalContext } from '../../context/ModalContext';
import useToast from '../../hooks/useToast';
import useGet from '../../apiHooks/useGet';
import usePost from '../../apiHooks/usePost';

interface OnClickDesignatedProps {
  topicId: number;
  topicName: string;
}

const AddToMyTopicList = ({ pin }: any) => {
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
    const url = '/pins';
    const payload = {
      topicId: topic.topicId,
      name: pin.name,
      description: pin.description,
      address: pin.address,
      latitude: pin.latitude,
      longitude: pin.longitude,
      legalDongCode: '',
    };

    fetchPost(
      {
        url,
        payload,
      },
      '내 지도에 핀 추가를 실패하였습니다. 잠시 후 다시 시도해주세요.',
      () => {
        closeModal('addToMyTopicList');
        showToast('info', '내 지도에 핀이 추가되었습니다.');
      },
    );
  };

  if (!myTopics) return <></>;

  return (
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
  );
};

const ModalMyTopicListWrapper = styled.ul`
  width: 684px;
  margin: 0 auto;
  display: flex;
  flex-wrap: wrap;
  gap: 20px;
`;

export default AddToMyTopicList;

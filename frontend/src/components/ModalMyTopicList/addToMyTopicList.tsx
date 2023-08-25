import { Fragment, useContext, useEffect, useState } from 'react';
import { ModalTopicCardProps } from '../../types/Topic';
import { getApi } from '../../apis/getApi';
import { styled } from 'styled-components';
import ModalTopicCard from '../ModalTopicCard';
import { ModalContext } from '../../context/ModalContext';
import { postApi } from '../../apis/postApi';
import useToast from '../../hooks/useToast';

const AddToMyTopicList = ({ pin }: any) => {
  const [myTopics, setMyTopics] = useState<ModalTopicCardProps[]>([]);
  const { closeModal } = useContext(ModalContext);
  const { showToast } = useToast();

  const getMyTopicFromServer = async () => {
    const serverMyTopic = await getApi<ModalTopicCardProps[]>(
      '/members/my/topics',
    );
    setMyTopics(serverMyTopic);
  };

  useEffect(() => {
    getMyTopicFromServer();
  }, []);

  const addPinToTopic = async (topicId: any) => {
    try {
      await postApi(`/pins`, {
        topicId: topicId.topicId,
        name: pin.name,
        description: pin.description,
        address: pin.address,
        latitude: pin.latitude,
        longitude: pin.longitude,
        legalDongCode: '',
      });
      closeModal('addToMyTopicList');
      showToast('info', '내 지도에 핀이 추가되었습니다.');
    } catch (error) {
      showToast('error', '내 지도에 핀 추가를 실패했습니다.');
    }
  };
  if (!myTopics) return <></>;

  return (
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
            topicClick={addPinToTopic}
            topicBookmarkCount={topic.bookmarkCount}
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

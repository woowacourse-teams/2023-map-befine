import { Fragment, useContext, useEffect, useState } from 'react';
import { ModalMyTopicType } from '../../types/Topic';
import { getApi } from '../../apis/getApi';
import { styled } from 'styled-components';
import ModalTopicCard from '../ModalTopicCard';
import { ModalContext } from '../../context/ModalContext';
import { postApi } from '../../apis/postApi';

const AddToMyTopicList = ({ pin }: any) => {
  const [myTopics, setMyTopics] = useState<ModalMyTopicType[]>([]);
  const { closeModal } = useContext(ModalContext);
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
  console.log(pin, 'PIN');
  const addPinToTopic = async (topicId: any) => {
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
            topicUpdatedAt={topic.updatedAt}
            topicPinCount={topic.pinCount}
            topicClick={addPinToTopic}
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

export default AddToMyTopicList;

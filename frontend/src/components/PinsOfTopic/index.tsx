import { styled } from 'styled-components';
import { TopicDetailProps } from '../../types/Topic';
import PinPreview from '../PinPreview';
import TopicInfo from '../TopicInfo';

interface PinsOfTopicProps {
  topicId: string;
  idx: number;
  topicDetail: TopicDetailProps;
  setSelectedPinId: React.Dispatch<React.SetStateAction<number | null>>;
  setIsEditPinDetail: React.Dispatch<React.SetStateAction<boolean>>;
  setTopicsFromServer: () => void;
}

const PinsOfTopic = ({
  topicId,
  idx,
  topicDetail,
  setSelectedPinId,
  setIsEditPinDetail,
  setTopicsFromServer,
}: PinsOfTopicProps) => {
  return (
    <Wrapper>
      <TopicInfo
        topicId={topicId}
        idx={idx}
        topicImage={topicDetail.image}
        topicTitle={topicDetail.name}
        topicCreator={topicDetail.creator}
        topicUpdatedAt={topicDetail.updatedAt}
        topicPinCount={topicDetail.pinCount}
        topicBookmarkCount={topicDetail.bookmarkCount}
        topicDescription={topicDetail.description}
        canUpdate={topicDetail.canUpdate}
        isInAtlas={topicDetail.isInAtlas}
        isBookmarked={topicDetail.isBookmarked}
        setTopicsFromServer={setTopicsFromServer}
      />
      {topicDetail.pins.map((pin, idx) => (
        <li key={pin.id}>
          <PinPreview
            idx={idx}
            pinTitle={pin.name}
            pinLocation={pin.address}
            pinInformation={pin.description}
            setSelectedPinId={setSelectedPinId}
            pinId={Number(pin.id)}
            topicId={topicId}
            setIsEditPinDetail={setIsEditPinDetail}
          />
        </li>
      ))}
    </Wrapper>
  );
};

const Wrapper = styled.ul``;

export default PinsOfTopic;

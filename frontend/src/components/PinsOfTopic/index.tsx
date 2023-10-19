import { styled } from 'styled-components';

import { TopicDetailProps } from '../../types/Topic';
import PinPreview from '../PinPreview';
import TopicInfo from '../TopicInfo';

interface PinsOfTopicProps {
  urlTopicId?: string;
  topicId: string;
  topicDetail: TopicDetailProps;
  setSelectedPinId: React.Dispatch<React.SetStateAction<number | null>>;
  setIsEditPinDetail: React.Dispatch<React.SetStateAction<boolean>>;
  setTopicsFromServer: () => void;
}

function PinsOfTopic({
  urlTopicId,
  topicId,
  topicDetail,
  setSelectedPinId,
  setIsEditPinDetail,
  setTopicsFromServer,
}: PinsOfTopicProps) {
  return (
    <Wrapper>
      <TopicInfo
        topicId={topicId}
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
            urlTopicId={urlTopicId || topicId}
            idx={idx}
            pinId={Number(pin.id)}
            topicId={topicId}
            pinTitle={pin.name}
            pinLocation={pin.address}
            pinInformation={pin.description}
            setSelectedPinId={setSelectedPinId}
            setIsEditPinDetail={setIsEditPinDetail}
          />
        </li>
      ))}
    </Wrapper>
  );
}

const Wrapper = styled.ul``;

export default PinsOfTopic;

import { useContext, useEffect, useState } from 'react';
import Space from '../components/common/Space';
import Flex from '../components/common/Flex';
import PinPreview from '../components/PinPreview';
import TopicInfo from '../components/TopicInfo';
import { TopicInfoType } from '../types/Topic';
import { useLocation, useParams, useSearchParams } from 'react-router-dom';
import theme from '../themes';
import PinDetail from './PinDetail';
import { getApi } from '../utils/getApi';
import { MergeOrSeeTogether } from '../components/MergeOrSeeTogether';
import { TagIdContext } from '../store/TagId';
import useNavigator from '../hooks/useNavigator';
import { TopicsIdContext } from '../store/TopicsId';

const SelectedTopic = () => {
  // const { topicId } = useParams();
  const { state } = useLocation();
  const [tagPins, setTagPins] = useState<string[]>([]);

  const { routePage } = useNavigator();

  const { topicsId, setTopicsId } = useContext(TopicsIdContext) ?? {
    topicsId: [],
    setTopicsId: () => {},
  };

  const { tagId, setTagId } = useContext(TagIdContext) ?? {
    tagId: [],
    setTagId: () => {},
  };

  if (state !== null) setTopicsId(state);

  const [searchParams, setSearchParams] = useSearchParams();
  const [topicDetail, setTopicDetail] = useState<TopicInfoType[]>([]);
  const [selectedPinId, setSelectedPinId] = useState<number | null>(null);

  const getAndSetDataFromServer = async () => {
    const data: TopicInfoType[] = [];
    for (const topicId of topicsId) {
      data.push(await getApi(`/topics/${topicId}`));
      setTopicDetail([...data]);
    }
  };

  const onClickConfirm = () => {
    routePage('/new-topic', 'pins');
  };

  const onTagCancel = () => {
    setTagPins([]);
    setTagId([]);
  };

  useEffect(() => {
    getAndSetDataFromServer();

    const queryParams = new URLSearchParams(location.search);
    if (queryParams.has('pinDetail')) {
      setSelectedPinId(Number(queryParams.get('pinDetail')));
    }
  }, [searchParams, topicsId]);

  useEffect(() => {
    if (tagPins.length === 0) setTagId([]);
  }, [tagPins, state]);

  if (!topicDetail) return <></>;

  return (
    <>
      <Flex $flexDirection="column">
        <Space size={2} />
        {tagPins.length > 0 ? (
          <MergeOrSeeTogether
            tag={tagPins}
            confirmButton="뽑아오기"
            onClickConfirm={onClickConfirm}
            onClickClose={onTagCancel}
          />
        ) : null}
        <ul>
          {topicDetail.length !== 0 ? (
            topicDetail.map((topic) => {
              return (
                <li key={topic.id}>
                  <TopicInfo
                    topicParticipant={12}
                    pinNumber={topic.pinCount}
                    topicTitle={topic.name}
                    topicOwner={'하지원'}
                    topicDescription={topic.description}
                  />
                  {topic.pins.map((pin) => (
                    <li key={pin.id}>
                      <Space size={3} />
                      <PinPreview
                        pinTitle={pin.name}
                        pinLocation={pin.address}
                        pinInformation={pin.description}
                        setSelectedPinId={setSelectedPinId}
                        pinId={Number(pin.id)}
                        topicId={topic.id}
                        tagPins={tagPins}
                        setTagPins={setTagPins}
                      />
                    </li>
                  ))}
                </li>
              );
            })
          ) : (
            <></>
          )}
        </ul>

        {selectedPinId && (
          <Flex
            $backgroundColor="white"
            width="400px"
            $minHeight="100vh"
            position="absolute"
            left="400px"
            top="0px"
            padding={4}
            $flexDirection="column"
            $borderLeft={`1px solid ${theme.color.gray}`}
          >
            <PinDetail pinId={selectedPinId} />
          </Flex>
        )}
      </Flex>
    </>
  );
};

export default SelectedTopic;

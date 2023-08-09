import { useContext, useEffect, useState } from 'react';
import { styled } from 'styled-components';
import Space from '../components/common/Space';
import Flex from '../components/common/Flex';
import PinPreview from '../components/PinPreview';
import TopicInfo from '../components/TopicInfo';
import { TopicInfoType } from '../types/Topic';
import { useParams, useSearchParams } from 'react-router-dom';
import theme from '../themes';
import PinDetail from './PinDetail';
import { getApi } from '../apis/getApi';
import { MergeOrSeeTogether } from '../components/MergeOrSeeTogether';
import { CoordinatesContext } from '../context/CoordinatesContext';
import useNavigator from '../hooks/useNavigator';
import { LayoutWidthContext } from '../context/LayoutWidthContext';

const SelectedTopic = () => {
  const { topicId } = useParams();
  const [tagPins, setTagPins] = useState<string[]>([]);
  const [searchParams, _] = useSearchParams();
  const [topicDetail, setTopicDetail] = useState<TopicInfoType[]>([]);
  const [selectedPinId, setSelectedPinId] = useState<number | null>(null);
  const [taggedPinIds, setTaggedPinIds] = useState<number[]>([]);
  const [isOpen, setIsOpen] = useState(true);
  const { routePage } = useNavigator();
  const { setCoordinates } = useContext(CoordinatesContext);
  const { setWidth } = useContext(LayoutWidthContext);

  const getAndSetDataFromServer = async () => {
    const data = await getApi(
      'default',
      `/topics/ids?ids=${topicId?.split(',').join('&ids=')}`,
    );
    const topicHashmap = new Map([]);

    // 각 topic의 pin들의 좌표를 가져옴
    const newCoordinates: any = [];

    data.forEach((topic: any) => {
      topic.pins.forEach((pin: any) => {
        newCoordinates.push({
          id: pin.id,
          topicId: topic.id,
          latitude: pin.latitude,
          longitude: pin.longitude,
        });
      });
    });

    setCoordinates(newCoordinates);

    data.forEach((topicDetailFromData: TopicInfoType) =>
      topicHashmap.set(`${topicDetailFromData.id}`, topicDetailFromData),
    );

    const topicDetailFromData = topicId
      ?.split(',')
      .map((number) => topicHashmap.get(number)) as TopicInfoType[];

    if (!topicDetailFromData) return;

    setTopicDetail([...topicDetailFromData]);
  };

  const onClickConfirm = () => {
    routePage('/new-topic', taggedPinIds.join(','));
  };

  const onTagCancel = () => {
    setTagPins([]);
    setTaggedPinIds([]);
  };

  useEffect(() => {
    getAndSetDataFromServer();

    // setWidth('400px');

    const queryParams = new URLSearchParams(location.search);
    if (queryParams.has('pinDetail')) {
      setSelectedPinId(Number(queryParams.get('pinDetail')));
    }
    setIsOpen(true);
  }, [searchParams]);

  const togglePinDetail = () => {
    setIsOpen(!isOpen);
  };

  if (!topicDetail) return <></>;
  if (!tagPins) return <></>;

  return (
    <>
      <Flex $flexDirection="column">
        <Space size={2} />
        {taggedPinIds.length > 0 && (
          <MergeOrSeeTogether
            tag={tagPins}
            confirmButton="뽑아오기"
            onClickConfirm={onClickConfirm}
            onClickClose={onTagCancel}
          />
        )}
        {topicDetail.length !== 0 ? (
          topicDetail.map((topic, idx) => {
            return (
              <ul key={topic.id}>
                {idx !== 0 && <Space size={5} />}
                <TopicInfo
                  fullUrl={topicId}
                  topicId={topicId?.split(',')[idx]}
                  topicParticipant={1}
                  pinNumber={topic.pinCount}
                  topicTitle={topic.name}
                  topicOwner={'토픽을 만든 사람'}
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
                      topicId={topicId}
                      tagPins={tagPins}
                      setTagPins={setTagPins}
                      taggedPinIds={taggedPinIds}
                      setTaggedPinIds={setTaggedPinIds}
                    />
                  </li>
                ))}
              </ul>
            );
          })
        ) : (
          <></>
        )}

        {selectedPinId && (
          <>
            <ToggleButton $isCollapsed={!isOpen} onClick={togglePinDetail}>
              ◀
            </ToggleButton>
            <PinDetailWrapper className={isOpen ? '' : 'collapsedPinDetail'}>
              <Flex
                $backgroundColor="white"
                width="400px"
                height="100vh"
                overflow="auto"
                position="absolute"
                left="400px"
                top="0px"
                padding={4}
                $flexDirection="column"
                $borderLeft={`1px solid ${theme.color.gray}`}
                $zIndex={1}
              >
                <PinDetail pinId={selectedPinId} />
              </Flex>
            </PinDetailWrapper>
          </>
        )}
      </Flex>
    </>
  );
};

const PinDetailWrapper = styled.div`
  &.collapsedPinDetail {
    z-index: -1;
  }
`;

const ToggleButton = styled.button<{ $isCollapsed: boolean }>`
  position: absolute;
  top: 50%;
  left: 800px;
  transform: translateY(-50%);
  z-index: 1;
  height: 80px;
  background-color: #fff;
  padding: 12px;
  border-radius: 4px;
  box-shadow: 0px 1px 5px rgba(0, 0, 0, 0.2);
  cursor: pointer;

  ${(props) =>
    props.$isCollapsed &&
    `
    transform: rotate(180deg);
    top:45%;
    left: 400px;
    z-index: 1;
    `}

  &:hover {
    background-color: #f5f5f5;
  }
`;

export default SelectedTopic;

import { useContext, useEffect, useState } from 'react';
import { styled } from 'styled-components';
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
import { CoordinatesContext } from '../context/CoordinatesContext';
import useNavigator from '../hooks/useNavigator';

const PinDetailWrapper = styled.div`
  &.collapsedPinDetail {
    z-index: -1;
  }
`;

const ToggleButton = styled.button<{ isCollapsed: boolean }>`
  position: absolute;
  top: 50%;
  left: 800px;
  transform: translateY(-50%);
  z-index: 99999;
  height: 80px;
  background-color: #fff;
  padding: 12px;
  border-radius: 4px;
  box-shadow: 0px 1px 5px rgba(0, 0, 0, 0.2);
  cursor: pointer;

  ${(props) =>
    props.isCollapsed &&
    `
    transform: rotate(180deg);
    top:45%;
    left: 400px;
    z-index: 99999;
    `}

  &:hover {
    background-color: #f5f5f5;
  }
`;

const SelectedTopic = () => {
  const { topicId } = useParams();
  const { state } = useLocation();
  const [tagPins, setTagPins] = useState<string[]>([]);

  const [searchParams, setSearchParams] = useSearchParams();
  const [topicDetail, setTopicDetail] = useState<TopicInfoType[]>([]);
  const [selectedPinId, setSelectedPinId] = useState<number | null>(null);

  const { routePage } = useNavigator();
  const { setCoordinates } = useContext(CoordinatesContext);

  const [isOpen, setIsOpen] = useState(true);

  const { tagId, setTagId } = useContext(TagIdContext);

  const onClickSetSelectedPinId = (pinId: number) => {
    setSelectedPinId(pinId);

    routePage(`/topics/${topicId}?pinDetail=${pinId}`);
  };

  const getAndSetDataFromServer = async () => {
    const data = await getApi(
      `/topics/ids?ids=${topicId?.split(',').join('&ids=')}`,
    );

    data.forEach((data: any) => {
      setCoordinates((prev) => [...prev, ...data.pins]);
    });

    setTopicDetail([...data]);
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
    setIsOpen(true);
  }, [searchParams]);

  useEffect(() => {
    if (tagPins.length === 0) setTagId([]);
  }, [tagPins, state]);

  const togglePinDetail = () => {
    setIsOpen(!isOpen);
  };

  if (!topicDetail) return <></>;
  if (!tagPins) return <></>;

  return (
    <>
      <Flex $flexDirection="column">
        <Space size={2} />
        {tagPins.length > 0 && (
          <MergeOrSeeTogether
            tag={tagPins}
            confirmButton="뽑아오기"
            onClickConfirm={onClickConfirm}
            onClickClose={onTagCancel}
          />
        )}
        <ul>
          {topicDetail.length !== 0 ? (
            topicDetail.map((topic, index) => {
              return (
                <li key={topic.id}>
                  {index !== 0 && <Space size={5} />}
                  <TopicInfo
                    topicParticipant={1}
                    pinNumber={topic.pinCount}
                    topicTitle={topic.name}
                    topicOwner={'하지원'}
                    topicDescription={topic.description}
                  />
                  {topic.pins.map((pin) => (
                    <li
                      key={pin.id}
                      onClick={() => {
                        onClickSetSelectedPinId(Number(pin.id));
                        setIsOpen(true);
                      }}
                    >
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
          <>
            <ToggleButton isCollapsed={!isOpen} onClick={togglePinDetail}>
              ◀
            </ToggleButton>
            <PinDetailWrapper className={isOpen ? '' : 'collapsedPinDetail'}>
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
                $zIndex={99}
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

export default SelectedTopic;

import { useContext, useEffect, useState } from 'react';
import Space from '../components/common/Space';
import Flex from '../components/common/Flex';
import PinPreview from '../components/PinPreview';
import TopicInfo from '../components/TopicInfo';
import { TopicInfoType } from '../types/Topic';
import { useParams, useSearchParams } from 'react-router-dom';
import theme from '../themes';
import PinDetail from './PinDetail';
import { getApi } from '../utils/getApi';
import useNavigator from '../hooks/useNavigator';
import { CoordinatesContext } from '../context/CoordinatesContext';
import { styled } from 'styled-components';

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
  const { routePage } = useNavigator();
  const [searchParams] = useSearchParams();
  const [topicDetail, setTopicDetail] = useState<TopicInfoType | null>(null);
  const [selectedPinId, setSelectedPinId] = useState<string | null>(null);
  const { setCoordinates } = useContext(CoordinatesContext);

  const onClickSetSelectedPinId = (pinId: string) => {
    setSelectedPinId(pinId);

    routePage(`/topics/${topicId}?pinDetail=${pinId}`);
  };

  const getAndSetDataFromServer = async () => {
    const data = await getApi(`/topics/${topicId}`);
    // context coordinates에 data.pins의 좌표들을 넣어주기
    setCoordinates(data.pins);
    console.log(data.pins);
    setTopicDetail(data);
  };

  useEffect(() => {
    getAndSetDataFromServer();

    const queryParams = new URLSearchParams(location.search);
    if (queryParams.has('pinDetail')) {
      setSelectedPinId(queryParams.get('pinDetail'));
    }
    setIsOpen(true);
  }, [searchParams]);

  const [isOpen, setIsOpen] = useState(true);

  const togglePinDetail = () => {
    setIsOpen(!isOpen);
  };

  if (!topicDetail) return <></>;

  return (
    <>
      <Flex $flexDirection="column">
        <ul>
          <TopicInfo
            topicParticipant={12}
            pinNumber={topicDetail.pinCount}
            topicTitle={topicDetail.name}
            topicOwner={'하지원'}
            topicDescription={topicDetail.description}
          />
          {topicDetail.pins.map((pin) => (
            <li
              key={pin.id}
              onClick={() => {
                onClickSetSelectedPinId(pin.id);
                setIsOpen(true);
              }}
            >
              <Space size={3} />
              <PinPreview
                pinTitle={pin.name}
                pinLocation={pin.address}
                pinInformation={pin.description}
              />
            </li>
          ))}
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

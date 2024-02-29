import { lazy, Suspense, useContext, useEffect, useRef, useState } from 'react';
import { useParams, useSearchParams } from 'react-router-dom';
import { styled } from 'styled-components';

import Space from '../../components/common/Space';
import PullPin from '../../components/PullPin';
import PinsOfTopicSkeleton from '../../components/Skeletons/PinsOfTopicSkeleton';
import { LAYOUT_PADDING, SIDEBAR } from '../../constants';
import { CoordinatesContext } from '../../context/CoordinatesContext';
import useResizeMap from '../../hooks/useResizeMap';
import useSetLayoutWidth from '../../hooks/useSetLayoutWidth';
import useSetNavbarHighlight from '../../hooks/useSetNavbarHighlight';
import useTags from '../../hooks/useTags';
import useMapStore from '../../store/mapInstance';
import PinDetail from '../PinDetail';
import useClusterCoordinates from './hooks/useClusterCoordinates';
import useTopicDetailQuery from './hooks/useTopicDetailQuery';

const PinsOfTopic = lazy(() => import('../../components/PinsOfTopic'));

function SelectedTopic() {
  const { topicId } = useParams() as { topicId: string };
  const [searchParams, _] = useSearchParams();
  const [selectedPinId, setSelectedPinId] = useState<number | null>(null);
  const [isOpen, setIsOpen] = useState(true);
  const [isEditPinDetail, setIsEditPinDetail] = useState<boolean>(false);
  const { setCoordinates } = useContext(CoordinatesContext);
  const { width } = useSetLayoutWidth(SIDEBAR);
  const zoomTimerIdRef = useRef<NodeJS.Timeout | null>(null);
  const dragTimerIdRef = useRef<NodeJS.Timeout | null>(null);
  const { mapInstance } = useMapStore((state) => state);

  const { data: topicDetail, refetch: getTopicDetail } =
    useTopicDetailQuery(topicId);
  const setClusteredCoordinates = useClusterCoordinates(topicId);
  const { tags, setTags, onClickInitTags, onClickCreateTopicWithTags } =
    useTags();

  useSetNavbarHighlight('none');
  useResizeMap();

  const setPrevCoordinates = () => {
    setCoordinates((prev) => [...prev]);
  };

  const adjustMapDirection = () => {
    if (!mapInstance) return;

    mapInstance.setBearing(0);
    mapInstance.setPitch(0);
  };

  useEffect(() => {
    setTags([]);
  }, []);

  useEffect(() => {
    setClusteredCoordinates();

    const onDragEnd = (evt: evt) => {
      if (dragTimerIdRef.current) {
        clearTimeout(dragTimerIdRef.current);
      }

      dragTimerIdRef.current = setTimeout(() => {
        setPrevCoordinates();
        adjustMapDirection();
      }, 100);
    };
    const onZoomEnd = (evt: evt) => {
      if (zoomTimerIdRef.current) {
        clearTimeout(zoomTimerIdRef.current);
      }

      zoomTimerIdRef.current = setTimeout(() => {
        setClusteredCoordinates();
        adjustMapDirection();
      }, 100);
    };

    if (!mapInstance) return;

    mapInstance.on('DragEnd', onDragEnd);
    mapInstance.on('ZoomEnd', onZoomEnd);

    return () => {
      mapInstance.off('DragEnd', onDragEnd);
      mapInstance.off('ZoomEnd', onZoomEnd);
    };
  }, [topicDetail]);

  useEffect(() => {
    const queryParams = new URLSearchParams(location.search);

    if (queryParams.has('pinDetail')) {
      setSelectedPinId(Number(queryParams.get('pinDetail')));
      setIsOpen(true);
      return;
    }

    setSelectedPinId(null);
  }, [searchParams]);

  if (!topicId || !topicDetail) return <></>;

  return (
    <Wrapper
      width={`calc(${width} - ${LAYOUT_PADDING})`}
      $selectedPinId={selectedPinId}
    >
      <Space size={3} />
      {tags.length > 0 && (
        <PullPin
          tags={tags}
          confirmButton="뽑아오기"
          onClickConfirm={onClickCreateTopicWithTags}
          onClickClose={onClickInitTags}
        />
      )}
      <Suspense fallback={<PinsOfTopicSkeleton />}>
        <PinsOfTopic
          topicId={topicId}
          topicDetail={topicDetail}
          setSelectedPinId={setSelectedPinId}
          setIsEditPinDetail={setIsEditPinDetail}
          setTopicsFromServer={getTopicDetail}
        />
      </Suspense>

      <Space size={8} />

      {selectedPinId && (
        <>
          <ToggleButton
            $isCollapsed={!isOpen}
            onClick={() => {
              setIsOpen(!isOpen);
            }}
            aria-label={`장소 상세 설명 버튼 ${isOpen ? '닫기' : '열기'}`}
          >
            ◀
          </ToggleButton>
          <PinDetailWrapper className={isOpen ? '' : 'collapsedPinDetail'}>
            <PinDetail
              width={width}
              pinId={selectedPinId}
              isEditPinDetail={isEditPinDetail}
              setIsEditPinDetail={setIsEditPinDetail}
            />
          </PinDetailWrapper>
        </>
      )}
    </Wrapper>
  );
}

const Wrapper = styled.section<{
  width: 'calc(100vw - 40px)' | 'calc(372px - 40px)';
  $selectedPinId: number | null;
}>`
  display: flex;
  flex-direction: column;
  width: ${({ width }) => width};
  margin: 0 auto;

  @media (max-width: 1076px) {
    width: ${({ $selectedPinId }) => ($selectedPinId ? '49vw' : '50vw')};
    margin: ${({ $selectedPinId }) => $selectedPinId && '0'};
  }

  @media (max-width: 744px) {
    width: 100%;
  }
`;

const PinDetailWrapper = styled.div`
  &.collapsedPinDetail {
    z-index: -1;
  }
`;

const ToggleButton = styled.button<{
  $isCollapsed: boolean;
}>`
  position: absolute;
  top: 50%;
  left: 744px;
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
    top: 45%;
    left: 372px;
    z-index: 1;
    `}

  &:hover {
    background-color: #f5f5f5;
  }

  @media (max-width: 1076px) {
    display: none;
  }
`;

export default SelectedTopic;

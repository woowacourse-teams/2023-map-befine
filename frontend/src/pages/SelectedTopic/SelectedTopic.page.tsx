import { lazy, Suspense, useState } from 'react';
import { useParams } from 'react-router-dom';
import { styled } from 'styled-components';

import Space from '../../components/common/Space';
import PullPin from '../../components/PullPin';
import PinsOfTopicSkeleton from '../../components/Skeletons/PinsOfTopicSkeleton';
import { LAYOUT_PADDING, SIDEBAR } from '../../constants';
import useResizeMap from '../../hooks/useResizeMap';
import useSetLayoutWidth from '../../hooks/useSetLayoutWidth';
import useSetNavbarHighlight from '../../hooks/useSetNavbarHighlight';
import useTags from '../../hooks/useTags';
import PinDetail from '../PinDetail';
import useClusterCoordinates from './hooks/useClusterCoordinates';
import useHandleMapInteraction from './hooks/useHandleMapInteraction';
import useSetSelectedPinId from './hooks/useSetSelectedPinId';
import useTopicDetailQuery from './hooks/useTopicDetailQuery';

const PinsOfTopic = lazy(() => import('../../components/PinsOfTopic'));

function SelectedTopic() {
  const { topicId } = useParams() as { topicId: string };
  const [isEditPinDetail, setIsEditPinDetail] = useState<boolean>(false);
  const { width } = useSetLayoutWidth(SIDEBAR);
  const { tags, onClickInitTags, onClickCreateTopicWithTags } = useTags({ isInitTags: true });
  const { isDoubleSidebarOpen, selectedPinId, setIsDoubleSidebarOpen, setSelectedPinId } = useSetSelectedPinId();
  const { data: topicDetail, refetch: getTopicDetail } = useTopicDetailQuery(topicId);
  const setClusteredCoordinates = useClusterCoordinates(topicId);

  useHandleMapInteraction({
    topicId,
    onAfterInteraction: setClusteredCoordinates,
  });
  useSetNavbarHighlight('none');
  useResizeMap();

  if (!topicId || !topicDetail) return <></>;

  return (
    <Wrapper width={`calc(${width} - ${LAYOUT_PADDING})`} $selectedPinId={selectedPinId}>
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
            $isCollapsed={!isDoubleSidebarOpen}
            onClick={() => {
              setIsDoubleSidebarOpen(!isDoubleSidebarOpen);
            }}
            aria-label={`장소 상세 설명 버튼 ${isDoubleSidebarOpen ? '닫기' : '열기'}`}
          >
            ◀
          </ToggleButton>
          <PinDetailWrapper className={isDoubleSidebarOpen ? '' : 'collapsedPinDetail'}>
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
import { KeyboardEvent, useContext, useEffect, useRef, useState } from 'react';
import { useLocation } from 'react-router-dom';
import { styled } from 'styled-components';

import { TagContext } from '../../context/TagContext';
import useAriaLive from '../../hooks/useAriaLive';
import useNavigator from '../../hooks/useNavigator';
import theme from '../../themes';
import Box from '../common/Box';
import Flex from '../common/Flex';
import Space from '../common/Space';
import Text from '../common/Text';

export interface PinPreviewProps {
  idx: number;
  pinId: number;
  topicId: string;
  urlTopicId: string;
  pinTitle: string;
  pinLocation: string;
  pinInformation: string;
  setSelectedPinId: React.Dispatch<React.SetStateAction<number | null>>;
  setIsEditPinDetail: React.Dispatch<React.SetStateAction<boolean>>;
}

function PinPreview({
  idx,
  pinTitle,
  pinLocation,
  pinInformation,
  setSelectedPinId,
  pinId,
  topicId,
  urlTopicId,
  setIsEditPinDetail,
}: PinPreviewProps) {
  const { pathname } = useLocation();
  const { routePage } = useNavigator();
  const { tags, setTags } = useContext(TagContext);
  const inputRef = useRef<HTMLInputElement | null>(null);
  const { setAriaLiveTagInnerText } = useAriaLive({
    liveTagId: 'live-region',
    defaultAnnounceText: '지도 핀 선택',
  });

  const addTagsAnnounceText = () => {
    if (tags.length === 0) {
      setAriaLiveTagInnerText(
        `핀 ${pinTitle}이 뽑아오기 목록에 추가됨. 뽑아오기 기능을 활성화 합니다.`,
      );
      return;
    }

    setAriaLiveTagInnerText(`핀 ${pinTitle}이 뽑아오기 목록에 추가됨`);
  };

  const deleteTagsAnnounceText = () => {
    if (tags.length === 1) {
      setAriaLiveTagInnerText(
        `핀 ${pinTitle}이 뽑아오기 목록에서 삭제됨. 뽑아오기 기능을 비활성화 합니다.`,
      );
      return;
    }
    setAriaLiveTagInnerText(`핀 ${pinTitle}이 뽑아오기 목록에서 삭제됨`);
  };

  const onAddTagOfTopic = (e: React.ChangeEvent<HTMLInputElement>) => {
    e.stopPropagation();

    if (e.target.checked) {
      setTags((prevTags) => [...prevTags, { id: pinId, title: pinTitle }]);

      addTagsAnnounceText();
    } else {
      setTags(tags.filter((tag) => tag.id !== pinId));

      deleteTagsAnnounceText();
    }
  };

  const onClickSetSelectedPinId = () => {
    setSelectedPinId(pinId);
    setIsEditPinDetail(false);

    if (pathname.split('/')[1] === 'topics') {
      routePage(`/topics/${urlTopicId}?pinDetail=${pinId}`);
      return;
    }

    routePage(`/see-together/${urlTopicId}?pinDetail=${pinId}`);
  };

  const onInputKeyDown = (e: KeyboardEvent<HTMLInputElement>) => {
    if (e.keyCode === 13) {
      e.preventDefault();
      inputRef.current?.click();
    }
  };

  return (
    <Flex
      height="152px"
      position="relative"
      $justifyContent="space-between"
      $backgroundColor="white"
      $borderTop={idx === 0 ? `1px solid ${theme.color.lightGray}` : ''}
      $borderBottom={`1px solid ${theme.color.lightGray}`}
    >
      <Flex
        $flexDirection="column"
        cursor="pointer"
        onClick={onClickSetSelectedPinId}
        width="88%"
        height="95%"
        tabIndex={0}
        role="button"
      >
        <Space size={1} />
        <EllipsisTitle
          color="black"
          $fontSize="default"
          $fontWeight="bold"
          aria-label={`장소 이름은 ${pinTitle} 이고`}
        >
          {pinTitle}
        </EllipsisTitle>
        <Space size={0} />
        <Text
          color="gray"
          $fontSize="small"
          $fontWeight="normal"
          aria-label={`장소의 위치는 ${pinLocation} 입니다.`}
        >
          {pinLocation}
        </Text>
        <Space size={3} />
        <EllipsisDescription
          color="black"
          $fontSize="small"
          $fontWeight="normal"
          aria-label={`이 장소에 대한 미리보기 설명은 다음과 같습니다. ${pinInformation}`}
        >
          {pinInformation}
        </EllipsisDescription>
      </Flex>
      <Box>
        <Space size={0} />
        <MultiSelectButton
          type="checkbox"
          onChange={onAddTagOfTopic}
          onKeyDown={onInputKeyDown}
          checked={Boolean(tags.map((tag) => tag.id).includes(pinId))}
          aria-label={`${pinTitle} 장소 뽑아오기 선택`}
          ref={inputRef}
        />
      </Box>
      <div
        id="live-region"
        aria-live="assertive"
        style={{ position: 'absolute', left: '-9999px' }}
      />
    </Flex>
  );
}

const MultiSelectButton = styled.input`
  width: 24px;
  height: 24px;
  cursor: pointer;

  -webkit-appearance: none;
  appearance: none;
  border-radius: 0.15em;
  margin-right: 0.5em;
  border: 0.1em solid ${({ theme }) => theme.color.black};
  background-color: ${({ theme }) => theme.color.white};
  outline: none;

  &:checked {
    border-color: transparent;
    background-image: url("data:image/svg+xml,%3csvg viewBox='0 0 16 16' fill='white' xmlns='http://www.w3.org/2000/svg'%3e%3cpath d='M5.707 7.293a1 1 0 0 0-1.414 1.414l2 2a1 1 0 0 0 1.414 0l4-4a1 1 0 0 0-1.414-1.414L7 8.586 5.707 7.293z'/%3e%3c/svg%3e");
    background-size: 100% 100%;
    background-position: 50%;
    background-repeat: no-repeat;
    background-color: ${({ theme }) => theme.color.checked};
  }
`;

const EllipsisTitle = styled(Text)`
  width: 100%;
  display: -webkit-box;
  word-wrap: break-word;
  -webkit-line-clamp: 1;
  -webkit-box-orient: vertical;
  text-overflow: ellipsis;
  overflow: hidden;
`;

const EllipsisDescription = styled(Text)`
  width: 100%;
  display: -webkit-box;
  word-wrap: break-word;
  -webkit-line-clamp: 2;
  -webkit-box-orient: vertical;
  text-overflow: ellipsis;
  overflow: hidden;
`;

export default PinPreview;

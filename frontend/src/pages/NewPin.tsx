/* eslint-disable no-nested-ternary */
import { FormEvent, useContext, useEffect, useState } from 'react';
import { useLocation } from 'react-router-dom';
import { styled } from 'styled-components';

import { getApi } from '../apis/getApi';
import { postApi } from '../apis/postApi';
import Button from '../components/common/Button';
import Flex from '../components/common/Flex';
import Autocomplete from '../components/common/Input/Autocomplete';
import Space from '../components/common/Space';
import Text from '../components/common/Text';
import InputContainer from '../components/InputContainer';
import Modal from '../components/Modal';
import ModalMyTopicList from '../components/ModalMyTopicList';
import { ARIA_FOCUS, LAYOUT_PADDING, SIDEBAR } from '../constants';
import { CoordinatesContext } from '../context/CoordinatesContext';
import { MarkerContext } from '../context/MarkerContext';
import { ModalContext } from '../context/ModalContext';
import useCompressImage from '../hooks/useCompressImage';
import useFormValues from '../hooks/useFormValues';
import useNavigator from '../hooks/useNavigator';
import useSetLayoutWidth from '../hooks/useSetLayoutWidth';
import useSetNavbarHighlight from '../hooks/useSetNavbarHighlight';
import useToast from '../hooks/useToast';
import { NewPinFormProps } from '../types/FormValues';
import { Poi } from '../types/Poi';
import { TopicCardProps } from '../types/Topic';
import { hasErrorMessage, hasNullValue } from '../validations';

type NewPinFormValueType = Pick<
  NewPinFormProps,
  'name' | 'address' | 'description'
>;

function NewPin() {
  const { state: topicId } = useLocation();
  const { navbarHighlights: _ } = useSetNavbarHighlight('addMapOrPin');
  const [topic, setTopic] = useState<any>(null);
  const [selectedTopic, setSelectedTopic] = useState<any>(null);
  const [showedImages, setShowedImages] = useState<string[]>([]);
  const { clickedMarker, markers, removeMarkers, removeInfowindows } =
    useContext(MarkerContext);
  const { clickedCoordinate, setClickedCoordinate } =
    useContext(CoordinatesContext);
  const { formValues, errorMessages, onChangeInput } =
    useFormValues<NewPinFormValueType>({
      name: '',
      address: '',
      description: '',
    });
  const { routePage } = useNavigator();
  const { showToast } = useToast();
  const { width } = useSetLayoutWidth(SIDEBAR);
  const { openModal } = useContext(ModalContext);
  const { compressImageList } = useCompressImage();

  const [formImages, setFormImages] = useState<File[]>([]);

  const goToBack = () => {
    routePage(-1);
  };

  const postToServer = async () => {
    let postTopicId = topic?.id;
    const postName = formValues.name;

    const formData = new FormData();

    if (!topic) {
      // 토픽이 없으면 selectedTopic을 통해 토픽을 생성한다.
      postTopicId = selectedTopic?.topicId;
    }

    if (topic?.length > 1) {
      postTopicId = selectedTopic.topicId;
    }

    formImages.forEach((file) => {
      formData.append('images', file);
    });

    const objectData = {
      topicId: postTopicId,
      name: postName,
      address: clickedCoordinate.address,
      description: formValues.description,
      latitude: clickedCoordinate.latitude,
      longitude: clickedCoordinate.longitude,
      legalDongCode: '',
    };

    const data = JSON.stringify(objectData);
    const jsonBlob = new Blob([data], { type: 'application/json' });

    formData.append('request', jsonBlob);

    await postApi('/pins', formData);
  };

  const onSubmit = async (e: FormEvent<HTMLFormElement>) => {
    e.preventDefault();

    if (clickedCoordinate.address?.length === 0) {
      showToast('error', '장소의 위치를 입력해주세요.');
      return;
    }

    if (hasErrorMessage(errorMessages) || hasNullValue(formValues, 'address')) {
      showToast('error', '입력하신 항목들을 다시 한 번 확인해주세요.');
      return;
    }

    try {
      await postToServer();

      showToast('info', `${formValues.name} 핀을 추가하였습니다.`);

      // 선택된 마커가 있으면 마커를 지도에서 제거
      if (clickedMarker) {
        clickedMarker.setMap(null);
      }

      setClickedCoordinate({
        latitude: 0,
        longitude: 0,
        address: '',
      });

      if (topicId && topicId.length > 1) {
        routePage(`/topics/${topicId}`, [topic!.id]);
        return;
      }
      let postTopicId = topic?.id;

      if (!topic) {
        // 토픽이 없으면 selectedTopic을 통해 토픽을 생성한다.
        postTopicId = selectedTopic?.topicId;
      }

      if (postTopicId) routePage(`/topics/${postTopicId}`, [postTopicId]);
    } catch {
      showToast(
        'error',
        '핀을 추가할 수 있는 권한이 없거나 지도를 선택하지 않았습니다.',
      );
    }
  };

  const onPinImageChange = async (
    event: React.ChangeEvent<HTMLInputElement>,
  ) => {
    const imageLists = event.target.files;
    let imageUrlLists = [...showedImages];

    if (!imageLists) {
      showToast(
        'error',
        '추가하신 이미지를 찾을 수 없습니다. 다시 선택해 주세요.',
      );
      return;
    }

    const compressedImageList = await compressImageList(imageLists);

    for (let i = 0; i < imageLists.length; i += 1) {
      const currentImageUrl = URL.createObjectURL(compressedImageList[i]);
      imageUrlLists.push(currentImageUrl);
    }

    if (imageUrlLists.length > 8) {
      showToast(
        'info',
        '이미지 개수는 최대 8개까지만 선택 가능합니다. 다시 선택해 주세요.',
      );
      imageUrlLists = imageUrlLists.slice(0, 8);
      return;
    }

    setFormImages([...formImages, ...compressedImageList]);
    setShowedImages(imageUrlLists);
  };

  const onSuggestionSelected = (suggestion: Poi) => {
    const { noorLat, noorLon } = suggestion;
    const address = `${suggestion.upperAddrName} ${suggestion.middleAddrName} ${suggestion.roadName}[${suggestion.name}]`;

    setClickedCoordinate({
      latitude: Number(noorLat),
      longitude: Number(noorLon),
      address,
    });
  };

  useEffect(() => {
    const getTopicId = async () => {
      if (topicId && topicId.split(',').length === 1) {
        const data = await getApi<TopicCardProps>(`/topics/${topicId}`);

        setTopic(data);
      }

      if (topicId && topicId.split(',').length > 1) {
        const topics = await getApi<any>(`/topics/ids?ids=${topicId}`);

        setTopic(topics);
      }
    };

    if (!topicId && markers && markers.length > 0) {
      removeMarkers();
      removeInfowindows();
    }

    getTopicId();
  }, []);

  return (
    <>
      <form onSubmit={onSubmit}>
        <Space size={4} />
        <Wrapper
          width={`calc(${width} - ${LAYOUT_PADDING})`}
          $flexDirection="column"
        >
          <Text
            as="h3"
            color="black"
            $fontSize="large"
            $fontWeight="bold"
            tabIndex={ARIA_FOCUS}
            aria-label="장소 생성 페이지입니다. 아래 항목을 입력하세요."
          >
            핀 생성
          </Text>

          <Space size={5} />

          <Text color="black" $fontSize="default" $fontWeight="normal">
            지도 선택
          </Text>

          <Space size={2} />

          <Button
            type="button"
            variant="primary"
            onClick={() => {
              if (topic && topic.name) return;
              openModal('newPin');
            }}
          >
            {topic?.name
              ? topic?.name
              : selectedTopic?.topicName
              ? selectedTopic?.topicName
              : '지도를 선택해주세요.'}
          </Button>

          <Space size={5} />

          <Text color="black" $fontSize="default" $fontWeight="normal">
            장소 사진
          </Text>
          <Text color="gray" $fontSize="small" $fontWeight="normal">
            장소에 대한 사진을 추가해주세요.
          </Text>
          <Space size={0} />
          <Flex>
            <ImageInputLabel
              htmlFor="file"
              tabIndex={ARIA_FOCUS}
              aria-label="장소에 해당하는 사진을 선택해주세요."
            >
              파일 찾기
            </ImageInputLabel>
            <ImageInputButton
              id="file"
              type="file"
              name="images"
              onChange={onPinImageChange}
              multiple
            />
          </Flex>
          <Space size={0} />
          <Flex $flexDirection="row" $flexWrap="wrap">
            {showedImages.map((image, idx) => (
              <div key={idx}>
                <ShowImage src={image} alt={`${image}-${idx}`} />
                <Space size={0} />
              </div>
            ))}
          </Flex>

          <Space size={5} />

          <InputContainer
            tagType="input"
            containerTitle="장소 이름"
            isRequired
            name="name"
            value={formValues.name}
            placeholder="50글자 이내로 장소의 이름을 입력해주세요."
            onChangeInput={onChangeInput}
            tabIndex={ARIA_FOCUS}
            errorMessage={errorMessages.name}
            maxLength={50}
          />

          <Space size={1} />

          <Flex>
            <Text color="black" $fontSize="default" $fontWeight="normal">
              장소 위치
            </Text>
            <Space size={0} />
            <Text color="primary" $fontSize="extraSmall" $fontWeight="normal">
              *
            </Text>
          </Flex>
          <Space size={0} />
          <Autocomplete
            defaultValue={clickedCoordinate.address}
            onSuggestionSelected={onSuggestionSelected}
          />

          <Space size={5} />

          <InputContainer
            tagType="textarea"
            containerTitle="장소 설명"
            isRequired
            name="description"
            value={formValues.description}
            placeholder="1000자 이내로 장소에 대한 의견을 남겨주세요."
            onChangeInput={onChangeInput}
            tabIndex={ARIA_FOCUS}
            errorMessage={errorMessages.description}
            maxLength={1000}
          />

          <Space size={6} />

          <Flex $justifyContent="end">
            <Button
              tabIndex={ARIA_FOCUS}
              aria-label="장소 생성 취소하기"
              type="button"
              variant="secondary"
              onClick={goToBack}
            >
              취소하기
            </Button>
            <Space size={3} />
            <Button
              tabIndex={ARIA_FOCUS}
              aria-label="장소 생성하기"
              variant="primary"
            >
              추가하기
            </Button>
          </Flex>
          <Space size={7} />
        </Wrapper>
      </form>

      <Modal
        modalKey="newPin"
        position="center"
        width="744px"
        height="512px"
        $dimmedColor="rgba(0,0,0,0.25)"
      >
        <ModalContentsWrapper>
          <Space size={5} />
          <Text color="black" $fontSize="extraLarge" $fontWeight="bold">
            내 지도 리스트
          </Text>
          <Text color="gray" $fontSize="small" $fontWeight="normal">
            핀을 저장할 지도를 선택해주세요.
          </Text>
          <Space size={4} />
          <ModalMyTopicList topicId={topicId} topicClick={setSelectedTopic} />
        </ModalContentsWrapper>
      </Modal>
    </>
  );
}

const Wrapper = styled(Flex)`
  margin: 0 auto;

  @media (max-width: 1076px) {
    width: calc(50vw - 40px);
  }

  @media (max-width: 744px) {
    width: ${({ width }) => width};
    margin: 0 auto;
  }
`;

const ModalContentsWrapper = styled.div`
  width: 100%;
  height: 100%;
  background-color: white;
  display: flex;
  flex-direction: column;
  align-items: center;
  overflow: scroll;
`;

const ImageInputLabel = styled.label`
  height: 40px;
  padding: 10px 10px;

  color: ${({ theme }) => theme.color.black};
  background-color: ${({ theme }) => theme.color.lightGray};

  font-size: ${({ theme }) => theme.fontSize.extraSmall};
  cursor: pointer;
`;

const ShowImage = styled.img`
  width: 80px;
  height: 80px;
`;

const ImageInputButton = styled.input`
  display: none;
`;

export default NewPin;

import { useContext, useEffect, useState } from 'react';
import { useLocation } from 'react-router-dom';
import styled from 'styled-components';

import usePost from '../apiHooks/usePost';
import AuthorityRadioContainer from '../components/AuthorityRadioContainer';
import Button from '../components/common/Button';
import Flex from '../components/common/Flex';
import Space from '../components/common/Space';
import Text from '../components/common/Text';
import InputContainer from '../components/InputContainer';
import { ARIA_FOCUS, LAYOUT_PADDING, SIDEBAR } from '../constants';
import { MarkerContext } from '../context/MarkerContext';
import { TagContext } from '../context/TagContext';
import useCompressImage from '../hooks/useCompressImage';
import useFormValues from '../hooks/useFormValues';
import useNavigator from '../hooks/useNavigator';
import useSetLayoutWidth from '../hooks/useSetLayoutWidth';
import useSetNavbarHighlight from '../hooks/useSetNavbarHighlight';
import useToast from '../hooks/useToast';
import { NewTopicFormProps } from '../types/FormValues';
import { hasErrorMessage, hasNullValue } from '../validations';

type NewTopicFormValuesType = Omit<NewTopicFormProps, 'topics'>;

function NewTopic() {
  const { routePage } = useNavigator();
  const { state: pulledPinIds } = useLocation();
  const { showToast } = useToast();
  const { width } = useSetLayoutWidth(SIDEBAR);
  const { navbarHighlights: _ } = useSetNavbarHighlight('addMapOrPin');
  const { setTags } = useContext(TagContext);
  const { fetchPost } = usePost();
  const { formValues, errorMessages, onChangeInput } =
    useFormValues<NewTopicFormValuesType>({
      name: '',
      description: '',
      image: '',
    });
  const { compressImage } = useCompressImage();
  const { markers, removeMarkers, removeInfowindows } =
    useContext(MarkerContext);

  const [isPrivate, setIsPrivate] = useState(false); // 혼자 볼 지도 :  같이 볼 지도
  const [isAllPermissioned, setIsAllPermissioned] = useState(true); // 모두 : 지정 인원
  const [authorizedMemberIds, setAuthorizedMemberIds] = useState<number[]>([]);

  const [showImage, setShowImage] = useState<string>('');
  const [formImage, setFormImage] = useState<File | null>(null);

  const goToBack = () => {
    routePage(-1);
  };

  const onSubmit = async (e: React.FormEvent<HTMLFormElement>) => {
    e.preventDefault();

    if (hasErrorMessage(errorMessages) || hasNullValue(formValues, 'image')) {
      showToast('error', '입력하신 항목들을 다시 한 번 확인해주세요.');
      return;
    }

    const topicId = await createTopic();

    if (topicId) {
      await addAuthorityToTopicWithGroupPermission(topicId);
      routePage(`/topics/${topicId}`);
    }
  };

  const createTopic = async () => {
    const response = await postToServer();
    const location = response?.headers.get('Location');

    if (location) {
      const topicIdFromLocation = location.split('/')[2];
      return Number(topicIdFromLocation);
    }
  };

  const postToServer = async () => {
    const formData = new FormData();

    if (formImage) {
      formData.append('image', formImage);
    }

    const objectData = {
      name: formValues.name,
      description: formValues.description,
      pins: pulledPinIds ? pulledPinIds.split(',') : [],
      publicity: isPrivate ? 'PRIVATE' : 'PUBLIC',
      permissionType:
        isAllPermissioned && !isPrivate ? 'ALL_MEMBERS' : 'GROUP_ONLY',
    };

    const data = JSON.stringify(objectData);
    const jsonBlob = new Blob([data], { type: 'application/json' });

    formData.append('request', jsonBlob);

    return fetchPost({
      url: '/topics/new',
      payload: formData,
      errorMessage:
        '지도 생성에 실패하였습니다. 입력하신 항목들을 다시 확인해주세요.',
      onSuccess: () => {
        showToast('info', `${formValues.name} 지도를 생성하였습니다.`);
      },
    });
  };

  const addAuthorityToTopicWithGroupPermission = async (topicId: number) => {
    if (isAllPermissioned) return;

    fetchPost({
      url: '/permissions',
      payload: {
        topicId,
        memberIds: authorizedMemberIds,
      },
      errorMessage: `${formValues.name} 지도의 권한 설정에 실패했습니다.`,
    });
  };

  const onTopicImageFileChange = async (
    event: React.ChangeEvent<HTMLInputElement>,
  ) => {
    const file = event.target.files && event.target.files[0];
    const currentImage = new Image();
    if (!file) {
      showToast(
        'error',
        '추가하신 이미지를 찾을 수 없습니다. 다시 선택해 주세요.',
      );
      return;
    }

    const compressedFile = await compressImage(file);
    currentImage.src = URL.createObjectURL(compressedFile);

    currentImage.onload = () => {
      if (currentImage.width < 300) {
        showToast(
          'error',
          '이미지의 크기가 너무 작습니다. 다른 이미지를 선택해 주세요.',
        );
        return;
      }

      setFormImage(compressedFile);
      setShowImage(URL.createObjectURL(file));
    };
  };

  useEffect(() => {
    if (!pulledPinIds && markers && markers.length > 0) {
      removeMarkers();
      removeInfowindows();
    }
  }, []);

  return (
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
          aria-label="지도 생성 페이지입니다. 아래 항목을 입력하세요."
        >
          지도 생성
        </Text>

        <Space size={5} />

        <Text color="black" $fontSize="default" $fontWeight="normal">
          지도 사진
        </Text>
        <Text color="gray" $fontSize="small" $fontWeight="normal">
          지도를 대표할 수 있는 사진을 추가해주세요.
        </Text>
        <Space size={0} />
        <Flex>
          {showImage && (
            <>
              <ShowImage src={showImage} alt="사진 이미지" /> <Space size={2} />{' '}
            </>
          )}

          <ImageInputLabel
            htmlFor="file"
            role="button"
            tabIndex={ARIA_FOCUS}
            aria-label="지도를 대표하는 사진을 선택해주세요."
          >
            파일 찾기
          </ImageInputLabel>
          <ImageInputButton
            id="file"
            type="file"
            name="image"
            onChange={onTopicImageFileChange}
          />
        </Flex>

        <Space size={5} />

        <InputContainer
          tagType="input"
          containerTitle="지도 이름"
          isRequired
          name="name"
          value={formValues.name}
          placeholder="20자 이내로 지도의 이름을 입력해주세요."
          onChangeInput={onChangeInput}
          tabIndex={ARIA_FOCUS}
          errorMessage={errorMessages.name}
          maxLength={20}
        />

        <Space size={1} />

        <InputContainer
          tagType="textarea"
          containerTitle="한 줄 설명"
          isRequired
          name="description"
          value={formValues.description}
          placeholder="100글자 이내로 지도에 대해서 설명해주세요."
          onChangeInput={onChangeInput}
          tabIndex={ARIA_FOCUS}
          errorMessage={errorMessages.description}
          maxLength={100}
        />

        <Space size={1} />

        <AuthorityRadioContainer
          isPrivate={isPrivate}
          isAllPermissioned={isAllPermissioned}
          authorizedMemberIds={authorizedMemberIds}
          setIsPrivate={setIsPrivate}
          setIsAllPermissioned={setIsAllPermissioned}
          setAuthorizedMemberIds={setAuthorizedMemberIds}
        />

        <Space size={6} />

        <Flex $justifyContent="end">
          <Button
            tabIndex={ARIA_FOCUS}
            aria-label="지도 생성 취소하기"
            type="button"
            variant="secondary"
            onClick={goToBack}
          >
            취소하기
          </Button>
          <Space size={3} />
          <Button
            tabIndex={ARIA_FOCUS}
            aria-label="지도 생성하기"
            variant="primary"
            onClick={() => {
              setTags([]);
            }}
          >
            생성하기
          </Button>
        </Flex>
        <Space size={7} />
      </Wrapper>
    </form>
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

export default NewTopic;

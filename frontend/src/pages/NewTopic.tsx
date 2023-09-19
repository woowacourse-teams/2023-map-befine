import { useContext, useState } from 'react';
import Text from '../components/common/Text';
import Flex from '../components/common/Flex';
import Space from '../components/common/Space';
import Button from '../components/common/Button';
import useNavigator from '../hooks/useNavigator';
import { NewTopicFormProps } from '../types/FormValues';
import useFormValues from '../hooks/useFormValues';
import { useLocation } from 'react-router-dom';
import useToast from '../hooks/useToast';
import InputContainer from '../components/InputContainer';
import { hasErrorMessage, hasNullValue } from '../validations';
import useSetLayoutWidth from '../hooks/useSetLayoutWidth';
import { LAYOUT_PADDING, SIDEBAR } from '../constants';
import useSetNavbarHighlight from '../hooks/useSetNavbarHighlight';
import { TagContext } from '../context/TagContext';
import usePost from '../apiHooks/usePost';
import AuthorityRadioContainer from '../components/AuthorityRadioContainer';
import styled from 'styled-components';
import useCompressImage from '../hooks/useCompressImage';

type NewTopicFormValuesType = Omit<NewTopicFormProps, 'topics'>;

const NewTopic = () => {
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

  const [isPrivate, setIsPrivate] = useState(false); // 혼자 볼 지도 :  같이 볼 지도
  const [isAll, setIsAll] = useState(true); // 모두 : 지정 인원
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
      permissionType: isAll && !isPrivate ? 'ALL_MEMBERS' : 'GROUP_ONLY',
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
    if (isAll) return;

    fetchPost({
      url: '/permissions',
      payload: {
        topicId,
        memberIds: isPrivate ? [] : authorizedMemberIds,
      },
      errorMessage: `${formValues.name} 지도의 권한 설정에 실패했습니다.`,
    });
  };

  const onTopicImageFileChange = async (
    event: React.ChangeEvent<HTMLInputElement>,
  ) => {
    const file = event.target.files && event.target.files[0];
    if (!file) {
      showToast(
        'error',
        '추가하신 이미지를 찾을 수 없습니다. 다시 선택해 주세요.',
      );
      return;
    }

    const compressedFile = await compressImage(file);

    setFormImage(compressedFile);
    setShowImage(URL.createObjectURL(file));
  };

  return (
    <form onSubmit={onSubmit}>
      <Space size={4} />
      <Flex
        width={`calc(${width} - ${LAYOUT_PADDING})`}
        $flexDirection="column"
      >
        <Text color="black" $fontSize="large" $fontWeight="bold">
          지도 생성
        </Text>

        <Space size={2} />
        <Flex>
          {showImage && <ShowImage src={showImage} alt={`사진 이미지`} />}
          <ImageInputLabel htmlFor="file">파일업로드</ImageInputLabel>
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
          containerTitle="지도 이미지"
          isRequired={false}
          name="image"
          value={formValues.image}
          placeholder="이미지 URL을 입력해주세요."
          onChangeInput={onChangeInput}
          tabIndex={1}
          autoFocus
          errorMessage={errorMessages.image}
          maxLength={2048}
        />

        <Space size={1} />

        <InputContainer
          tagType="input"
          containerTitle="지도 이름"
          isRequired={true}
          name="name"
          value={formValues.name}
          placeholder="20자 이내로 지도의 이름을 입력해주세요."
          onChangeInput={onChangeInput}
          tabIndex={2}
          errorMessage={errorMessages.name}
          maxLength={20}
        />

        <Space size={1} />

        <InputContainer
          tagType="textarea"
          containerTitle="한 줄 설명"
          isRequired={true}
          name="description"
          value={formValues.description}
          placeholder="100글자 이내로 지도에 대해서 설명해주세요."
          onChangeInput={onChangeInput}
          tabIndex={3}
          errorMessage={errorMessages.description}
          maxLength={100}
        />

        <Space size={1} />

        <AuthorityRadioContainer
          isPrivate={isPrivate}
          isAll={isAll}
          authorizedMemberIds={authorizedMemberIds}
          setIsPrivate={setIsPrivate}
          setIsAll={setIsAll}
          setAuthorizedMemberIds={setAuthorizedMemberIds}
        />

        <Space size={6} />

        <Flex $justifyContent="end">
          <Button
            tabIndex={7}
            type="button"
            variant="secondary"
            onClick={goToBack}
          >
            취소하기
          </Button>
          <Space size={3} />
          <Button
            tabIndex={7}
            variant="primary"
            onClick={() => {
              setTags([]);
            }}
          >
            생성하기
          </Button>
        </Flex>
      </Flex>
    </form>
  );
};

const ImageInputLabel = styled.label`
  height: 40px;
  margin-left: 10px;
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

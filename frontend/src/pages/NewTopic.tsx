import Text from '../components/common/Text';
import Flex from '../components/common/Flex';
import Space from '../components/common/Space';
import Button from '../components/common/Button';
import { postApi } from '../apis/postApi';
import useNavigator from '../hooks/useNavigator';
import { NewTopicFormValuesType } from '../types/FormValues';
import useFormValues from '../hooks/useFormValues';
import { useLocation } from 'react-router-dom';
import useToast from '../hooks/useToast';
import InputContainer from '../components/InputContainer';

const DEFAULT_IMAGE =
  'https://velog.velcdn.com/images/semnil5202/post/37dae18f-9860-4483-bad5-1158a210e5a8/image.svg';

const NewTopic = () => {
  const { formValues, errorMessages, onChangeInput } = useFormValues<
    Omit<NewTopicFormValuesType, 'topics'>
  >({
    name: '',
    description: '',
    image: '',
  });
  const { routePage } = useNavigator();
  const { state: taggedIds } = useLocation();
  const { showToast } = useToast();

  const goToBack = () => {
    routePage(-1);
  };

  const onSubmit = async (e: React.FormEvent<HTMLFormElement>) => {
    e.preventDefault();

    if (
      Object.values(errorMessages).some(
        (errorMessage) => errorMessage.length > 0,
      )
    ) {
      showToast('error', '입력하신 항목들을 다시 한 번 확인해주세요.');
      return;
    }

    const topicId = await postToServer();
    if (topicId) routePage(`/topics/${topicId}`);
  };

  const postToServer = async () => {
    const response =
      taggedIds?.length > 1 && typeof taggedIds !== 'string'
        ? await mergeTopics()
        : await createTopic();

    const location = response.headers.get('Location');

    if (location) {
      const topicIdFromLocation = location.split('/')[2];
      return topicIdFromLocation;
    }
  };

  const mergeTopics = async () => {
    showToast('info', `${formValues.name} 토픽을 병합하였습니다.`);

    return await postApi('/topics/merge', {
      image: formValues.image || DEFAULT_IMAGE,
      name: formValues.name,
      description: formValues.description,
      topics: taggedIds,
    });
  };

  const createTopic = async () => {
    showToast('info', `${formValues.name} 토픽을 생성하였습니다.`);

    return await postApi('/topics/new', {
      image: formValues.image || DEFAULT_IMAGE,
      name: formValues.name,
      description: formValues.description,
      pins: typeof taggedIds === 'string' ? taggedIds.split(',') : [],
    });
  };

  return (
    <form onSubmit={onSubmit}>
      <Space size={4} />
      <Flex $flexDirection="column">
        <Text color="black" $fontSize="large" $fontWeight="bold">
          토픽 생성
        </Text>

        <Space size={5} />

        <InputContainer
          tagType="input"
          containerTitle="지도 이미지"
          isRequired={false}
          name="image"
          value={formValues.image}
          placeholder="원하시는 이미지가 있을 경우 이미지 URL을 입력해주세요."
          onChangeInput={onChangeInput}
          tabIndex={1}
          errorMessage={errorMessages.image}
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
          autoFocus
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
        />

        <Space size={6} />

        <Flex $justifyContent="end">
          <Button
            tabIndex={5}
            type="button"
            variant="secondary"
            onClick={goToBack}
          >
            취소하기
          </Button>
          <Space size={3} />
          <Button tabIndex={4} variant="primary">
            생성하기
          </Button>
        </Flex>
      </Flex>
    </form>
  );
};

export default NewTopic;

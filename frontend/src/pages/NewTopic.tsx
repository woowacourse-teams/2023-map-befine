import Input from '../components/common/Input';
import Text from '../components/common/Text';
import Flex from '../components/common/Flex';
import Space from '../components/common/Space';
import Button from '../components/common/Button';
import Textarea from '../components/common/Textarea';
import { postApi } from '../apis/postApi';
import useNavigator from '../hooks/useNavigator';
import { NewTopicFormValuesType } from '../types/FormValues';
import useFormValues from '../hooks/useFormValues';
import { useLocation } from 'react-router-dom';

const DEFAULT_IMAGE =
  'https://velog.velcdn.com/images/semnil5202/post/37dae18f-9860-4483-bad5-1158a210e5a8/image.svg';

const NewTopic = () => {
  const { formValues, onChangeInput } = useFormValues<NewTopicFormValuesType>({
    name: '',
    description: '',
    image: '',
    topics: [],
  });
  const { routePage } = useNavigator();
  const { state: taggedIds } = useLocation();

  const goToBack = () => {
    routePage(-1);
  };

  const onSubmit = async (e: React.FormEvent<HTMLFormElement>) => {
    e.preventDefault();

    const topicId = await postToServer();
    if (topicId) routePage(`/topics/${topicId}`);
  };

  const postToServer = async () => {
    const response =
      taggedIds?.length > 1 && typeof taggedIds !== 'string'
        ? await postApi('/topics/merge', {
            image: formValues.image || DEFAULT_IMAGE,
            name: formValues.name,
            description: formValues.description,
            topics: taggedIds,
          })
        : await postApi('/topics/new', {
            image: formValues.image || DEFAULT_IMAGE,
            name: formValues.name,
            description: formValues.description,
            pins: typeof taggedIds === 'string' ? taggedIds.split(',') : [],
          });

    const location = response.headers.get('Location');

    if (location) {
      const topicIdFromLocation = location.split('/')[2];
      return topicIdFromLocation;
    }
  };

  return (
    <form onSubmit={onSubmit}>
      <Space size={6} />
      <Flex $flexDirection="column">
        <Text color="black" $fontSize="large" $fontWeight="bold">
          토픽 생성
        </Text>

        <Space size={5} />

        <section>
          <Flex>
            <Text color="black" $fontSize="default" $fontWeight="normal">
              토픽 이미지
            </Text>
            <Space size={0} />
          </Flex>
          <Space size={0} />
          <Input
            name="image"
            value={formValues.image}
            placeholder="원하는 배경이 있을 시 이미지 링크를 남겨주세요."
            onChange={onChangeInput}
            autoFocus={true}
            tabIndex={1}
          />
        </section>
        <Space size={5} />

        <section>
          <Flex>
            <Text color="black" $fontSize="default" $fontWeight="normal">
              토픽 이름
            </Text>
            <Space size={0} />
            <Text color="primary" $fontSize="extraSmall" $fontWeight="normal">
              *
            </Text>
          </Flex>
          <Space size={0} />
          <Input
            name="name"
            value={formValues.name}
            placeholder="지도를 클릭하거나 장소의 이름을 입력해주세요."
            onChange={onChangeInput}
            tabIndex={2}
          />
        </section>

        <Space size={5} />

        <section>
          <Flex>
            <Text color="black" $fontSize="default" $fontWeight="normal">
              한 줄 설명
            </Text>
            <Space size={0} />
            <Text color="primary" $fontSize="extraSmall" $fontWeight="normal">
              *
            </Text>
          </Flex>
          <Space size={0} />
          <Textarea
            name="description"
            value={formValues.description}
            placeholder="장소에 대한 의견을 자유롭게 남겨주세요."
            onChange={onChangeInput}
            tabIndex={3}
          />
        </section>

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

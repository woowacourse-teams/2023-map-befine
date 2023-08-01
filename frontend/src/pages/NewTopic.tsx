import Input from '../components/common/Input';
import Text from '../components/common/Text';
import Flex from '../components/common/Flex';
import Space from '../components/common/Space';
import Button from '../components/common/Button';
import Textarea from '../components/common/Textarea';
import { styled } from 'styled-components';
import { postApi } from '../utils/postApi';
import useNavigator from '../hooks/useNavigator';
import { NewTopicFormValuesType } from '../types/FormValues';
import useFormValues from '../hooks/useFormValues';
import { useContext, useEffect } from 'react';
import { TagIdContext } from '../store/TagId';
import { useLocation } from 'react-router-dom';

const NewTopic = () => {
  const { formValues, onChangeInput } = useFormValues<NewTopicFormValuesType>({
    name: '',
    description: '',
    image: '',
    topics: [],
  });
  const { routePage } = useNavigator();

  const { state } = useLocation();

  const { tagId, setTagId } = useContext(TagIdContext) ?? {
    tagId: [],
    setTagId: () => {},
  };

  const goToBack = () => {
    routePage(-1);
  };

  const onSubmit = async (e: React.FormEvent) => {
    e.preventDefault();

    const topicId = await postToServer();
    if (topicId) routePage(`/topics/${topicId}`, [Number(topicId)]);
  };

  const postToServer = async () => {
    if (state === 'topics') {
      const response = await postApi('/topics/merge', {
        image: formValues.image,
        name: formValues.name,
        description: formValues.description,
        topics: tagId,
      });

      const location = response.headers.get('Location');

      if (location) {
        const topicIdFromLocation = location.split('/')[2];
        return topicIdFromLocation;
      }
    } else {
      const response = await postApi('/topics/new', {
        image: formValues.image,
        name: formValues.name,
        description: formValues.description,
        pins: tagId,
      });

      const location = response.headers.get('Location');

      if (location) {
        const topicIdFromLocation = location.split('/')[2];
        return topicIdFromLocation;
      }
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
            <Text color="primary" $fontSize="extraSmall" $fontWeight="normal">
              *
            </Text>
          </Flex>
          <Space size={0} />
          <Input
            name="image"
            value={formValues.image}
            placeholder="이미지 링크를 남겨주세요."
            onChange={onChangeInput}
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
          />
        </section>

        <Space size={6} />

        <Flex $justifyContent="end">
          <Button variant="primary">생성하기</Button>
          <Space size={3} />
          <Button type="button" variant="secondary" onClick={goToBack}>
            취소하기
          </Button>
        </Flex>
      </Flex>
    </form>
  );
};

const TopicIcon = styled(Input)`
  display: none;
  position: relative;

  & + label {
    display: flex;
    justify-content: center;
    align-items: center;
    width: 52px;
    height: 52px;
    font-size: 36px;
    border: 1px solid ${({ theme }) => theme.color.black};
    border-radius: 4px;
    cursor: pointer;
  }

  &:checked + label {
    background-color: ${({ theme }) => theme.color.primary};
  }
`;

export default NewTopic;

import Box from '../components/common/Box';
import Input from '../components/common/Input';
import Text from '../components/common/Text';
import Flex from '../components/common/Flex';
import Space from '../components/common/Space';
import Button from '../components/common/Button';
import Textarea from '../components/common/Textarea';
import { styled } from 'styled-components';
import { Fragment, useState, useRef } from 'react';
import { useNavigate } from 'react-router-dom';
import { postApi } from '../utils/postApi';

const icons = ['🍛', '🏃‍♂️', '👩‍❤️‍👨', '💻', '☕️', '🚀'];

const NewTopic = () => {
  const [selectedTopicIcon, setSelectedTopicIcon] = useState<string>('');
  const [topicName, setTopicName] = useState<string>('');
  const [topicDescription, setTopicDescription] = useState<string>('');
  const navigator = useNavigate();

  const goToBack = () => {
    navigator(-1);
  };

  const onChangeTopicName = (e: React.ChangeEvent<HTMLInputElement>) => {
    setTopicName(e.target.value);
  };

  const onChangeTopicDescription = (
    e: React.ChangeEvent<HTMLTextAreaElement>,
  ) => {
    setTopicDescription(e.target.value);
  };

  const onSubmit = async (e: React.FormEvent) => {
    e.preventDefault();

    const formElement = e.currentTarget;

    const topicId = await postToServer();

    if (topicId) navigator(`/topics/${topicId}`);
  };

  const postToServer = async () => {
    const response = await postApi('/topics/new', {
      emoji: selectedTopicIcon,
      name: topicName,
      description: topicDescription,
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
              토픽 아이콘
            </Text>
            <Space size={0} />
            <Text color="primary" $fontSize="extraSmall" $fontWeight="normal">
              *
            </Text>
          </Flex>
          <Space size={0} />
          <Flex $justifyContent="space-between">
            {icons.map((icon, idx) => (
              <Fragment key={idx}>
                <TopicIcon
                  type="radio"
                  id={`checkbox-${idx}`}
                  name="topic-icon-radio"
                />
                <label
                  id="radio-label"
                  htmlFor={`checkbox-${idx}`}
                  data-icon={icon}
                  onClick={() => {
                    setSelectedTopicIcon(icon);
                  }}
                >
                  {icon}
                </label>
              </Fragment>
            ))}
          </Flex>
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
            placeholder="지도를 클릭하거나 장소의 이름을 입력해주세요."
            onChange={onChangeTopicName}
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
            placeholder="장소에 대한 의견을 자유롭게 남겨주세요."
            onChange={onChangeTopicDescription}
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

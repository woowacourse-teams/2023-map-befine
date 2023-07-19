import Box from '../components/common/Box';
import Input from '../components/common/Input';
import Text from '../components/common/Text';
import Flex from '../components/common/Flex';
import Space from '../components/common/Space';
import Button from '../components/common/Button';
import Textarea from '../components/common/Textarea';
import { styled } from 'styled-components';
import { Fragment, useRef, useState } from 'react';
import { useNavigate } from 'react-router-dom';

const icons = ['🍛', '🏃‍♂️', '👩‍❤️‍👨', '💻', '☕️', '🚀'];

const NewTopic = () => {
  const [topicName, setTopicName] = useState<String>('');
  const [topicDescription, setTopicDescription] = useState<String>('');
  const topicIconRef = useRef<HTMLLabelElement>(null);
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

  const onSubmit = (e: React.FormEvent) => {
    e.preventDefault();

    if (!topicIconRef.current) return;

    // TODO: POST 'topics/new' -> GET 'topics/{topicId}'
    // TODO: POST { topicIconRef.current.dataset.icon, topicName, topicDescription}
    const topicId = 10;
    navigator(`/topics/${topicId}`);
  };

  return (
    <form onSubmit={onSubmit}>
      <Space size={6} />
      <Flex $flexDirection="column">
        <Text color="black" $fontSize="large" $fontWeight="bold">
          토픽 생성
        </Text>

        <Space size={5} />

        <Box>
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
                  htmlFor={`checkbox-${idx}`}
                  data-icon={icon}
                  ref={topicIconRef}
                >
                  {icon}
                </label>
              </Fragment>
            ))}
          </Flex>
        </Box>

        <Space size={5} />

        <Box>
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
        </Box>

        <Space size={5} />

        <Box>
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
        </Box>

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

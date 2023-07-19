import Box from '../components/common/Box';
import Input from '../components/common/Input';
import Text from '../components/common/Text';
import Flex from '../components/common/Flex';
import Space from '../components/common/Space';
import Button from '../components/common/Button';
import Textarea from '../components/common/Textarea';
import { styled } from 'styled-components';
import { Fragment, useState } from 'react';
import { useNavigate } from 'react-router-dom';
import { postApi } from '../utils/postApi';

const icons = ['🍛', '🏃‍♂️', '👩‍❤️‍👨', '💻', '☕️', '🚀'];

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

const NewTopic = () => {
  const [topicId, setTopicId] = useState<string>();

  const navigate = useNavigate();

  const onSubmit = (e: React.FormEvent) => {
    e.preventDefault();
  };

  const postToServer = async () => {
    const id = await postApi('/topics/new', {});
    setTopicId(id.headers.get('Location')?.split('/')[2]);
  };

  const onClickButton = async () => {
    await postToServer();
    if(topicId) navigate(`/topics/${topicId}`, { state: topicId });
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
                <label htmlFor={`checkbox-${idx}`}>{icon}</label>
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
          <Input placeholder="지도를 클릭하거나 장소의 이름을 입력해주세요." />
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
          <Textarea placeholder="장소에 대한 의견을 자유롭게 남겨주세요." />
        </Box>

        <Space size={6} />

        <Flex $justifyContent="end">
          {/* TODO: topics/${topicId} */}
          <Button onClick={onClickButton} variant="primary">
            생성하기
          </Button>
          <Space size={3} />
          {/* TODO: prev page */}
          <Button variant="secondary">취소하기</Button>
        </Flex>
      </Flex>
    </form>
  );
};

export default NewTopic;

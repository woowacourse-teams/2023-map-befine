import { styled } from 'styled-components';
import LoginErrorIcon from '../assets/LoginErrorIcon.svg';
import Button from '../components/common/Button';
import Flex from '../components/common/Flex';
import Space from '../components/common/Space';
import Text from '../components/common/Text';
import useSetLayoutWidth from '../hooks/useSetLayoutWidth';
import { DEFAULT_PROD_URL, FULLSCREEN } from '../constants';

const AskLogin = () => {
  const { width } = useSetLayoutWidth(FULLSCREEN);

  const loginButtonClick = async () => {
    window.location.href = `${DEFAULT_PROD_URL}/oauth/kakao`;
  };

  return (
    <Flex
      $flexDirection="column"
      $justifyContent="center"
      $alignItems="center"
      width={`calc(${width}-40px)`}
      height="calc(var(--vh, 1vh) * 100)"
      overflow="hidden"
    >
      <LoginErrorIcon />
      <Space size={6} />
      <Flex
        $flexDirection="column"
        $justifyContent="center"
        $alignItems="center"
      >
        <Text color="black" $fontSize="extraLarge" $fontWeight="bold">
          나만의 지도를 만들어 보세요.
        </Text>
        <Space size={5} />
        <NotFoundButton variant="primary" onClick={loginButtonClick}>
          카카오로 시작하기
        </NotFoundButton>
      </Flex>
    </Flex>
  );
};

const NotFoundButton = styled(Button)`
  width: 270px;
  height: 56px;

  background-color: rgb(255, 220, 0);

  color: ${({ theme }) => theme.color.black};
  font-weight: ${({ theme }) => theme.fontWeight['bold']};
  border: 1px solid ${({ theme }) => theme.color.white};
`;

export default AskLogin;

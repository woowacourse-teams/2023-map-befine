import { styled } from 'styled-components';
import LoginErrorIcon from '../assets/LoginErrorIcon.svg';
import Button from '../components/common/Button';
import Flex from '../components/common/Flex';
import Space from '../components/common/Space';
import Text from '../components/common/Text';
import useSetLayoutWidth from '../hooks/useSetLayoutWidth';
import { DEFAULT_PROD_URL, FULLSCREEN } from '../constants';
import { getApi } from '../apis/getApi';
import useNavigator from '../hooks/useNavigator';

const API_URL =
  process.env.NODE_ENV === 'production'
    ? process.env.REACT_APP_API_DEFAULT_PROD
    : 'http://localhost:3000';

const AskLogin = () => {
  const { routePage } = useNavigator();
  const { width } = useSetLayoutWidth(FULLSCREEN);

  const loginButtonClick = async () => {
    if (API_URL === 'http://localhost:3000') {
      const data = await getApi<any>('default', '/login');

      localStorage.setItem('userToken', data.accessToken);
      localStorage.setItem('user', JSON.stringify(data.member));

      routePage('/');
      return;
    }

    window.location.href = `${API_URL}/oauth/kakao`;
  };

  return (
    <Flex
      $flexDirection="column"
      $justifyContent="center"
      $alignItems="center"
      width={`calc(${width}-40px)`}
      height="100vh"
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

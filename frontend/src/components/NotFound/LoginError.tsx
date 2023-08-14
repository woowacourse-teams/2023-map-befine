import { styled } from 'styled-components';
import LoginErrorIcon from '../../assets/LoginErrorIcon.svg';
import Button from '../common/Button';
import Flex from '../common/Flex';
import Space from '../common/Space';
import Text from '../common/Text';

const LoginError = () => {
  return (
    <Flex
      $flexDirection="column"
      $justifyContent="center"
      $alignItems="center"
      width="100vw"
      height="100vh"
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
        <NotFoundButton variant="primary">카카오로 시작하기</NotFoundButton>
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

export default LoginError;

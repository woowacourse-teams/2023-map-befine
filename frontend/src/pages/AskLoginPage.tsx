import { keyframes, styled } from 'styled-components';
import Image from '../components/common/Image';
import Text from '../components/common/Text';
import Space from '../components/common/Space';
import { DEFAULT_PROD_URL } from '../constants';

export default function AskLoginPage() {
  const loginButtonClick = () => {
    window.location.href = `${DEFAULT_PROD_URL}/oauth/kakao`;
  };

  return (
    <ErrorContainer>
      <Text $fontSize="extraLarge" $fontWeight="bold" color="primary">
        로그인해서 쓰면 더 재밌지롱~
      </Text>
      <Space size={5} />
      <RetryButton onClick={loginButtonClick}>
        <Image src="//k.kakaocdn.net/14/dn/btroDszwNrM/I6efHub1SN5KCJqLm1Ovx1/o.jpg" />
      </RetryButton>
    </ErrorContainer>
  );
}

const ErrorContainer = styled.div`
  display: flex;
  flex-direction: column;
  justify-content: center;
  align-items: center;
  width: 100%;
  height: 100vh;
  text-align: center;
`;

const pulseAnimation = keyframes`
  0% {
    transform: scale(1);
  }
  50% {
    transform: scale(1.2);
  }
  100% {
    transform: scale(1);
  }
`;

const RetryButton = styled.button`
  display: flex;
  justify-content: center;
  align-items: center;
  width: 200px;
  height: 200px;
  border: none;
  border-radius: 50%;
  background-color: #fee500;
  font-size: 16px;
  font-weight: 700;
  color: black;
  cursor: pointer;
  animation: ${pulseAnimation} 1.5s infinite;
`;

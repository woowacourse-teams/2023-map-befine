import { useEffect } from 'react';
import { useLocation } from 'react-router-dom';
import { getApi } from '../apis/getApi';
import { keyframes, styled } from 'styled-components';
import useNavigator from '../hooks/useNavigator';
import { LoginResponse } from '../types/Login';

export const handleOAuthKakao = async (code: string) => {
  const { routePage } = useNavigator();
  try {
    const url = `http://localhost:8080/oauth/login/kakao?code=${code}`;
    const data = await getApi<LoginResponse>('login', url);
    // 아마도 data.accessToken 이런식으로 정보가 담겨서 올듯??
    routePage('/');
  } catch (error) {
    window.alert('로그인 실패');
  }
};

const KakaoRedirectPage = () => {
  const location = useLocation();

  useEffect(() => {
    const searchParams = new URLSearchParams(location.search);
    const code = searchParams.get('code');
    if (code) {
      handleOAuthKakao(code);
    }
  }, [location]);

  return (
    <KakaoRedirectPageWrapper>
      <div>매튜도이매튜도이매튜도이</div>
      <Loader />
      <div>매튜도이매튜도이매튜도이</div>
    </KakaoRedirectPageWrapper>
  );
};

export default KakaoRedirectPage;

const KakaoRedirectPageWrapper = styled.div`
  display: flex;
  justify-content: center;
  align-items: center;
  height: 100vh;
`;

const spin = keyframes`
  0% {
    transform: rotate(0deg);
  }
  100% {
    transform: rotate(360deg);
  }
`;

const Loader = styled.div`
  border: 8px solid #f3f3f3;
  border-radius: 50%;
  border-top: 8px solid #3498db;
  width: 60px;
  height: 60px;
  animation: ${spin} 2s linear infinite;
`;

import { useEffect } from 'react';
import { useLocation } from 'react-router-dom';
import { getApi } from '../apis/getApi';
import { keyframes, styled } from 'styled-components';
import useNavigator from '../hooks/useNavigator';
import { LoginResponse } from '../types/Login';

export const handleOAuthKakao = async (code: string) => {
  const { routePage } = useNavigator();
  try {
    const url = `https://mapbefine.kro.kr/oauth/login/kakao?code=${code}`;
    console.log('line12');
    const data = await getApi<LoginResponse>('login', url);

    localStorage.setItem('userToken', data.accessToken);
    localStorage.setItem('user', JSON.stringify(data.member));

    window.alert('login process');
    routePage('/');
  } catch (error) {
    window.alert('로그인 실패');
  }
};

const KakaoRedirectPage = () => {
  const location = useLocation();
  console.log('location', location);
  useEffect(() => {
    const searchParams = new URLSearchParams(location.search);
    const code = searchParams.get('code');
    console.log('code', code);
    if (code) {
      console.log('ifCode', code);
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

import { useEffect } from 'react';
import { useLocation } from 'react-router-dom';
import { getApi } from '../apis/getApi';
import { keyframes, styled } from 'styled-components';

const KakaoRedirectPage = () => {
  const location = useLocation();

  const handleOAuthKakao = async (code: any) => {
    try {
      const url = `http://localhost:8080/oauth/login/kakao?code=${code}`;
      const data = await getApi('login', url);
      // 아마도 data.accessToken 이런식으로 정보가 담겨서 올듯??
      console.log('access_token' + data.access_token);
      window.alert('로그인 성공: ' + data);
    } catch (error) {
      window.alert('로그인 실패');
    }
  };

  useEffect(() => {
    const searchParams = new URLSearchParams(location.search);
    const code = searchParams.get('code');
    if (code) {
      alert('CODE = ' + code);
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

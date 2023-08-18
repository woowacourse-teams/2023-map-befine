import { useEffect } from 'react';
import { useLocation } from 'react-router-dom';
import { getApi } from '../apis/getApi';
import { keyframes, styled } from 'styled-components';
import useNavigator from '../hooks/useNavigator';
import { DEFAULT_PROD_URL } from '../constants';

// const API_URL =
//   process.env.NODE_ENV === 'production'
//     ? process.env.REACT_APP_API_DEFAULT_PROD
//     : process.env.REACT_APP_API_DEFAULT_DEV;

export const handleOAuthKakao = async (code: string) => {
  try {
    const url = `${DEFAULT_PROD_URL}/oauth/login/kakao?code=${code}`;
    const data = await getApi<any>('login', url);

    localStorage.setItem('userToken', data.accessToken);
    localStorage.setItem('user', JSON.stringify(data.member));
    location.reload();
  } catch (error) {
    window.alert('로그인에 실패하였습니다. 이메일 수집을 동의해주세요.');
  }
};

const KakaoRedirectPage = () => {
  const { routePage } = useNavigator();

  const routerLocation = useLocation();

  useEffect(() => {
    const searchParams = new URLSearchParams(routerLocation.search);
    const code = searchParams.get('code');
    const getCode = async (code: string) => {
      await handleOAuthKakao(code);
    };

    if (code) {
      getCode(code);
      routePage('/');
    }
  }, [routerLocation]);

  return (
    <KakaoRedirectPageWrapper>
      <Loader />
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

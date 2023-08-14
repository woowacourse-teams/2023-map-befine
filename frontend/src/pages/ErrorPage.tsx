import { useRouteError } from 'react-router-dom';
import { keyframes, styled } from 'styled-components';
import useNavigator from '../hooks/useNavigator';

interface Error {
  statusText: string;
  status: number;
}

export default function RootErrorPage() {
  const { routePage } = useNavigator();

  const error: Error = useRouteError() as Error;
  console.error(error);
  console.log(error.statusText, 'ERROR 잡음');
  return (
    <ErrorContainer>
      <h1>Oops!</h1>
      <p>Sorry, an unexpected error has occurred.</p>
      <RetryButton onClick={() => routePage('/')}>
        {error.status || error.statusText}
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
  width: 150px;
  height: 150px;
  border: none;
  border-radius: 50%;
  background-color: #ff3b3b;
  font-size: 16px;
  font-weight: 700;
  color: #ffffff;
  cursor: pointer;
  animation: ${pulseAnimation} 1.5s infinite;
`;

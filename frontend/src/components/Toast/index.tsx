import { useContext } from 'react';
import ReactDOM from 'react-dom';
import { keyframes, styled } from 'styled-components';
import { ToastContext } from '../../context/ToastContext';
import Flex from '../common/Flex';
import { TOAST_SHOWTIME } from '../../constants';

const asynchronousDelayTime = 50;

const Toast = () => {
  const { toast } = useContext(ToastContext);

  const root = document.querySelector('#root') as HTMLElement;

  return ReactDOM.createPortal(
    toast.show && (
      <Wrapper
        $justifyContent="center"
        $alignItems="center"
        type={toast.type}
        role="alert"
      >
        {toast.message}
      </Wrapper>
    ),
    root,
  );
};

const toastAnimation = keyframes`
  0% {
    transform: translate(-50%, 20px);
    opacity: 0;
  }

  8% {
    transform: translate(-50%, 0);
    opacity: 1;
  }

  100% {
    transform: translate(-50%, 0);
    opacity: 1;
  }
`;

const Wrapper = styled(Flex)<{ type: string }>`
  animation: ${toastAnimation} ${TOAST_SHOWTIME + asynchronousDelayTime}ms
    linear 1;
  position: absolute;
  left: 50%;
  bottom: 80px;
  transform: translateX(-50%);

  padding: 0 ${({ theme }) => theme.spacing[4]};
  height: ${({ theme }) => theme.spacing[6]};
  border-radius: ${({ theme }) => theme.radius.small};
  box-shadow: 2px 4px 4px 0px rgba(69, 69, 69, 0.75);

  background-color: ${({ type }) =>
    ({
      info: 'rgb(4, 192, 158)',
      warning: 'rgb(245, 213, 96)',
      error: 'rgb(222,96,96)',
    })[type]};

  color: ${({ theme }) => theme.color.white};

  z-index: 2;

  @media (max-width: 588px) {
    width: 80%;
  }
`;

export default Toast;

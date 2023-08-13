import { useContext, useEffect } from 'react';
import ReactDOM from 'react-dom';
import { css, keyframes, styled } from 'styled-components';
import { ModalContext } from '../../context/ModalContext';
type ModalWrapperType = Omit<ModalProps, 'children'>;
interface ModalProps {
  position: 'center' | 'bottom';
  width: string;
  height: string;
  children: React.ReactNode;
}
const Modal = ({ position, width, height, children }: ModalProps) => {
  const { isModalOpen, closeModal } = useContext(ModalContext);
  const root = document.querySelector('#root') as HTMLElement;
  useEffect(() => {
    const escKeyModalClose = (e: KeyboardEvent) => {
      if (e.key === 'Escape') {
        closeModal();
      }
    };
    window.addEventListener('keydown', escKeyModalClose);
    return () => {
      window.removeEventListener('keydown', escKeyModalClose);
    };
  }, []);
  const onClickDimmedCloseModal = (e: React.MouseEvent<HTMLDivElement>) => {
    closeModal();
  };
  return ReactDOM.createPortal(
    isModalOpen && (
      <>
        <WrapperDimmed onClick={onClickDimmedCloseModal} />
        <Wrapper id="modal" position={position} width={width} height={height}>
          {children}
        </Wrapper>
      </>
    ),
    root,
  );
};
const Wrapper = styled.div<ModalWrapperType>`
  width: ${({ width }) => width || '400px'};
  height: ${({ height }) => height || '400px'};
  ${({ position }) => getModalPosition(position)};
`;
const WrapperDimmed = styled.div`
  width: 100%;
  height: 100%;
  position: fixed;
  top: 0;
  background-color: rgba(0, 0, 0, 0.25);
`;
const translateModalAnimation = keyframes`
  from {
    opacity: 0;
    transform: translate(-50%, 100%);
  }
  to {
    opacity: 1;
    transform: translate(-50%, 0);
  }
`;
const openModalAnimation = keyframes`
  from {
    opacity: 0;
  }
  to {
    opacity: 1;
  }
`;
const getModalPosition = (position: 'center' | 'bottom' | 'absolute') => {
  switch (position) {
    case 'center':
      return css`
        position: fixed;
        top: 50%;
        left: 50%;
        transform: translate(-50%, -50%);
        border-radius: ${({ theme }) => theme.radius.medium};
        animation: ${openModalAnimation} 0.3s ease 1;
      `;
    case 'bottom':
      return css`
        position: fixed;
        left: 50%;
        transform: translate(-50%, 0);
        bottom: 0;
        border-top-left-radius: ${({ theme }) => theme.radius.medium};
        border-top-right-radius: ${({ theme }) => theme.radius.medium};
        animation: ${translateModalAnimation} 0.3s ease 1;
      `;
  }
};
export default Modal;

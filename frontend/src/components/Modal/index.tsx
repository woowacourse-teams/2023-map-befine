import { useContext, useEffect } from 'react';
import ReactDOM from 'react-dom';
import { css, keyframes, styled } from 'styled-components';
import { ModalContext } from '../../context/ModalContext';
import Box from '../common/Box';

type ModalWrapperType = Omit<ModalProps, 'children' | '$dimmedColor'>;

interface ModalProps {
  modalKey: string;
  position: 'center' | 'bottom';
  width: string;
  height: string;
  $dimmedColor: string;
  children: React.ReactNode;
  top?: string;
  left?: string;
  overflow?: string;
}

const Modal = ({
  modalKey,
  position,
  width,
  height,
  $dimmedColor,
  children,
  top,
  left,
  overflow,
}: ModalProps) => {
  const { modalOpens, closeModal } = useContext(ModalContext);

  const root = document.querySelector('#root') as HTMLElement;

  useEffect(() => {
    const escKeyModalClose = (e: KeyboardEvent) => {
      if (e.key === 'Escape') {
        closeModal(modalKey);
      }
    };

    window.addEventListener('keydown', escKeyModalClose);

    return () => {
      window.removeEventListener('keydown', escKeyModalClose);
    };
  }, []);

  const onClickDimmedCloseModal = (e: React.MouseEvent<HTMLDivElement>) => {
    closeModal(modalKey);
  };

  return ReactDOM.createPortal(
    modalOpens[modalKey] && (
      <Box role="dialog">
        <WrapperDimmed
          $dimmedColor={$dimmedColor}
          onClick={onClickDimmedCloseModal}
        />
        <Wrapper
          modalKey={modalKey}
          position={position}
          width={width}
          height={height}
          top={top}
          left={left}
          overflow={overflow}
        >
          {children}
        </Wrapper>
      </Box>
    ),
    root,
  );
};

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

const getModalPosition = (position: 'center' | 'bottom') => {
  switch (position) {
    case 'center':
      return css`
        position: fixed;
        top: 50%;
        left: 50%;
        transform: translate(-50%, -50%);
        border-radius: ${({ theme }) => theme.radius.medium};
        animation: ${openModalAnimation} 0.3s ease 1;
        overflow: scroll;
      `;

    case 'bottom':
      return css`
        position: fixed;
        left: 50%;
        bottom: 0;
        transform: translate(-50%, 0);
        border-top-left-radius: ${({ theme }) => theme.radius.medium};
        border-top-right-radius: ${({ theme }) => theme.radius.medium};
        animation: ${translateModalAnimation} 0.3s ease 1;
      `;
  }
};

const addMapOrPinPosition = (modalKey: string) => {
  if (modalKey === 'addMapOrPin') {
    return css`
      width: 252px;
      height: inherit;

      transform: translate(-50%, -30%);
    `;
  } else {
    return css`
      width: 100%;
      height: inherit;

      transform: translate(-50%, 0);
    `;
  }
};

const Wrapper = styled.div<ModalWrapperType>`
  width: ${({ width }) => width || '400px'};
  height: ${({ height }) => height || '400px'};
  ${({ position }) => getModalPosition(position)};
  top: ${({ top }) => top && top};
  left: ${({ left }) => left && left};
  z-index: 2;

  @media (max-width: 1076px) {
    ${getModalPosition('bottom')};
    width: 100%;
    height: inherit;
    ${({ modalKey }) => addMapOrPinPosition(modalKey)};
  }
`;

const WrapperDimmed = styled.div<{ $dimmedColor: string }>`
  width: 100%;
  height: 100%;
  position: fixed;
  top: 0;
  background-color: ${({ $dimmedColor }) => $dimmedColor};
  z-index: 2;
`;

export default Modal;

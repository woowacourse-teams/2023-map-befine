import React, {
  ReactChild,
  useContext,
  useEffect,
  useRef,
  useState,
} from 'react';
import ReactDOM from 'react-dom';
import { styled } from 'styled-components';

interface Props {
  children: ReactChild;
  closeModalHandler: () => void;
}

export const ModalPortal = (props: Props) => {
  const $modalRoot = document.getElementById('modal-root') as HTMLElement;
  const modalRef = useRef<HTMLDialogElement>(null);

  const dialogKeyDownListener = (
    event: React.KeyboardEvent<HTMLDialogElement>,
  ) => {
    if (event.key === 'Escape') {
      props.closeModalHandler();
    }
  };

  const dialogBackdropListener = (
    event: React.MouseEvent<HTMLDialogElement>,
  ) => {
    if (event.target === event.currentTarget) {
      props.closeModalHandler();
    }
  };

  useEffect(() => {
    modalRef.current?.showModal();

    return () => {
      modalRef.current?.close();
    };
  }, []);

  return ReactDOM.createPortal(
    <ModalContainer
      ref={modalRef}
      onKeyDown={dialogKeyDownListener}
      onClick={dialogBackdropListener}
    >
      {props.children}
    </ModalContainer>,
    $modalRoot ? $modalRoot : document.body,
  );
};

const ModalContainer = styled.dialog`
  width: 600px;
  max-height: 600px;
  padding: 32px 16px;

  display: flex;
  flex-direction: column;

  border-radius: 8px;
  background: #ffffff;

  border: 0px;

  overflow: scroll;
`;

interface ModalContextType {
  isModalOpen: boolean;
  openModal: () => void;
  closeModal: () => void;
}

export const useModalContext = () => {
  const modalContext = useContext(ModalContext);

  if (modalContext === null) {
    throw new Error('modalContext값이 null입니다.');
  }

  return modalContext;
};

export const ModalContext = React.createContext<ModalContextType | null>(null);

const ModalContextProvider = (props: { children: React.ReactNode }) => {
  const [isModalOpen, setIsModalOpen] = useState(false);

  const openModal = () => {
    setIsModalOpen(true);
  };

  const closeModal = () => {
    setIsModalOpen(false);
  };

  const contextValue: ModalContextType = {
    isModalOpen,
    openModal,
    closeModal,
  };

  return (
    <ModalContext.Provider value={contextValue}>
      {props.children}
    </ModalContext.Provider>
  );
};

export default ModalContextProvider;

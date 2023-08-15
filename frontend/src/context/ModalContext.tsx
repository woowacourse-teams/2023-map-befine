import { createContext, useState } from 'react';

interface ModalOpensProps {
  [key: string]: boolean;
}

interface ModalContextProps {
  modalOpens: ModalOpensProps;
  openModal: (key: string) => void;
  closeModal: (key: string) => void;
}

interface ModalProviderProps {
  children: React.ReactNode;
}

export const ModalContext = createContext<ModalContextProps>({
  modalOpens: {},
  openModal: () => {},
  closeModal: () => {},
});

const ModalProvider = ({ children }: ModalProviderProps) => {
  const [modalOpens, setModalOpens] = useState<ModalOpensProps>({});

  const openModal = (key: string) => {
    setModalOpens((prevState) => ({
      ...prevState,
      [key]: true,
    }));
  };

  const closeModal = (key: string) => {
    setModalOpens((prevState) => ({
      ...prevState,
      [key]: false,
    }));
  };

  return (
    <ModalContext.Provider
      value={{
        modalOpens,
        openModal,
        closeModal,
      }}
    >
      {children}
    </ModalContext.Provider>
  );
};

export default ModalProvider;

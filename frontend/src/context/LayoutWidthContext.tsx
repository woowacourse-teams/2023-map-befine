import {
  Dispatch,
  ReactNode,
  SetStateAction,
  createContext,
  useState,
} from 'react';

type LayoutWidthValue = '400px' | '100vw';

interface LayoutWidthContextProps {
  width: LayoutWidthValue;
  setWidth: Dispatch<SetStateAction<LayoutWidthValue>>;
}

interface LayoutWidthProviderProps {
  children: ReactNode;
}

export const LayoutWidthContext = createContext<LayoutWidthContextProps>({
  width: '400px',
  setWidth: () => {},
});

const LayoutWidthProvider = ({ children }: LayoutWidthProviderProps) => {
  const [width, setWidth] = useState<LayoutWidthValue>('400px');

  return (
    <LayoutWidthContext.Provider
      value={{
        width,
        setWidth,
      }}
    >
      {children}
    </LayoutWidthContext.Provider>
  );
};

export default LayoutWidthProvider;

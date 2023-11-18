import {
  createContext,
  Dispatch,
  ReactNode,
  SetStateAction,
  useState,
} from 'react';

type LayoutWidthValue = '372px' | '100vw';

interface LayoutWidthContextProps {
  width: LayoutWidthValue;
  setWidth: Dispatch<SetStateAction<LayoutWidthValue>>;
}

interface LayoutWidthProviderProps {
  children: ReactNode;
}

export const LayoutWidthContext = createContext<LayoutWidthContextProps>({
  width: '100vw',
  setWidth: () => {},
});

function LayoutWidthProvider({ children }: LayoutWidthProviderProps) {
  const [width, setWidth] = useState<LayoutWidthValue>('100vw');

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
}

export default LayoutWidthProvider;

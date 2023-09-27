import {
  Dispatch,
  ReactNode,
  SetStateAction,
  createContext,
  useState,
} from 'react';
import { TopicCardProps } from '../types/Topic';

interface SeeTogetherContextProps {
  seeTogetherTopics: number[] | null;
  setSeeTogetherTopics: Dispatch<SetStateAction<number[] | []>>;
}

interface SeeTogetherProviderProps {
  children: ReactNode;
}

export const SeeTogetherContext = createContext<SeeTogetherContextProps>({
  seeTogetherTopics: [],
  setSeeTogetherTopics: () => {},
});

const SeeTogetherProvider = ({ children }: SeeTogetherProviderProps) => {
  const [seeTogetherTopics, setSeeTogetherTopics] = useState<
    number[] | []
  >([]);

  return (
    <SeeTogetherContext.Provider
      value={{
        seeTogetherTopics,
        setSeeTogetherTopics,
      }}
    >
      {children}
    </SeeTogetherContext.Provider>
  );
};

export default SeeTogetherProvider;

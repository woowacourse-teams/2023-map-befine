import {
  Dispatch,
  ReactNode,
  SetStateAction,
  createContext,
  useState,
} from 'react';
import { TopicCardProps } from '../types/Topic';

interface SeeTogetherContextProps {
  seeTogetherTopics: TopicCardProps[] | null;
  setSeeTogetherTopics: Dispatch<SetStateAction<TopicCardProps[] | null>>;
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
    TopicCardProps[] | null
  >(null);

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

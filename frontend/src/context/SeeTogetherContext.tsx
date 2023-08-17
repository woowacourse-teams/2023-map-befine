import {
  Dispatch,
  ReactNode,
  SetStateAction,
  createContext,
  useState,
} from 'react';
import { TopicType } from '../types/Topic';

interface SeeTogetherContextProps {
  seeTogetherTopics: TopicType[] | null;
  setSeeTogetherTopics: Dispatch<SetStateAction<TopicType[] | null>>;
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
    TopicType[] | null
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

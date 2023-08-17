import {
  Dispatch,
  ReactNode,
  SetStateAction,
  createContext,
  useState,
} from 'react';
import { TagProps } from '../types/Tag';

interface TagContextProps {
  tags: TagProps[];
  setTags: Dispatch<SetStateAction<TagProps[]>>;
}

interface TagProviderProps {
  children: ReactNode;
}

export const TagContext = createContext<TagContextProps>({
  tags: [],
  setTags: () => {},
});

const TagProvider = ({ children }: TagProviderProps) => {
  const [tags, setTags] = useState<TagProps[]>([]);

  return (
    <TagContext.Provider
      value={{
        tags,
        setTags,
      }}
    >
      {children}
    </TagContext.Provider>
  );
};

export default TagProvider;

import { styled } from 'styled-components';
import { postApi } from '../../apis/postApi';
import useToast from '../../hooks/useToast';

interface AddFavoriteProps {
  id: number;
  children: React.ReactNode;
}

const AddFavorite = ({ id, children }: AddFavoriteProps) => {
  const { showToast } = useToast();

  const addFavoriteList = async (e: React.MouseEvent<HTMLDivElement>) => {
    e.stopPropagation();

    // TODO : post 후 전역 favorite List 에 담기, toast 메세지 수정
    // await postApi('',{});
    // await getApi('');
    await postApi(`/bookmarks/topics?id=${id}`, {});

    showToast('info', '즐겨찾기에 추가되었습니다.');
  };

  return <Wrapper onClick={addFavoriteList}>{children}</Wrapper>;
};

const Wrapper = styled.div`
  cursor: pointer;

  &:hover {
    filter: brightness(0.9);
  }
`;

export default AddFavorite;

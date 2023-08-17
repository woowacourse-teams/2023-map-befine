import { styled } from 'styled-components';
import { postApi } from '../../apis/postApi';
import useToast from '../../hooks/useToast';
import { deleteApi } from '../../apis/deleteApi';

interface AddFavoriteProps {
  id: number;
  isBookmarked: boolean;
  setTopicsFromServer: () => void;
  children: React.ReactNode;
}

const AddFavorite = ({
  id,
  isBookmarked,
  setTopicsFromServer,
  children,
}: AddFavoriteProps) => {
  const { showToast } = useToast();

  const addFavoriteList = async (e: React.MouseEvent<HTMLDivElement>) => {
    e.stopPropagation();

    try {
      await postApi(`/bookmarks/topics?id=${id}`, {}, 'x-www-form-urlencoded');

      setTopicsFromServer();

      showToast('info', '즐겨찾기에 추가되었습니다.');
    } catch {
      showToast(
        'error',
        '즐겨찾기 추가에 실패했습니다. 로그인 후 사용해주세요.',
      );
    }
  };

  const deleteFavoriteList = async (e: React.MouseEvent<HTMLDivElement>) => {
    e.stopPropagation();

    try {
      await deleteApi(`/bookmarks/topics?id=${id}`, 'x-www-form-urlencoded');

      setTopicsFromServer();

      showToast('info', '해당 지도를 즐겨찾기에서 제외했습니다.');
    } catch {
      showToast(
        'error',
        '즐겨찾기 추가에 실패했습니다. 로그인 후 사용해주세요.',
      );
    }

    await deleteApi(`/bookmarks/topics?id=${id}`, 'x-www-form-urlencoded');

    setTopicsFromServer();

    showToast('info', '해당 지도를 즐겨찾기에서 제외했습니다.');
  };

  return (
    <Wrapper onClick={isBookmarked ? deleteFavoriteList : addFavoriteList}>
      {children}
    </Wrapper>
  );
};

const Wrapper = styled.div`
  cursor: pointer;

  &:hover {
    filter: brightness(0.9);
  }
`;

export default AddFavorite;

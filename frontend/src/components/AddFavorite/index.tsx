import { styled } from 'styled-components';

import { deleteApi } from '../../apis/deleteApi';
import { postApi } from '../../apis/postApi';
import { ARIA_FOCUS } from '../../constants';
import useToast from '../../hooks/useToast';

interface AddFavoriteProps {
  id: number;
  isBookmarked: boolean;
  getTopicsFromServer: () => void;
  children: React.ReactNode;
}

function AddFavorite({
  id,
  isBookmarked,
  getTopicsFromServer,
  children,
}: AddFavoriteProps) {
  const { showToast } = useToast();

  const addFavoriteList = async (e: React.MouseEvent<HTMLDivElement>) => {
    e.stopPropagation();

    try {
      await postApi(`/bookmarks/topics?id=${id}`, {}, 'x-www-form-urlencoded');

      getTopicsFromServer();

      showToast('info', '즐겨찾기에 추가되었습니다.');
    } catch {
      showToast('error', '로그인 후 사용해주세요.');
    }
  };

  const deleteFavoriteList = async (e: React.MouseEvent<HTMLDivElement>) => {
    e.stopPropagation();

    try {
      await deleteApi(`/bookmarks/topics?id=${id}`, 'x-www-form-urlencoded');

      getTopicsFromServer();

      showToast('info', '해당 지도를 즐겨찾기에서 제외했습니다.');
    } catch {
      showToast('error', '로그인 후 사용해주세요.');
    }
  };

  return (
    <Wrapper
      tabIndex={ARIA_FOCUS}
      role="button"
      aria-label="즐겨찾기 추가 버튼"
      onClick={isBookmarked ? deleteFavoriteList : addFavoriteList}
    >
      {children}
    </Wrapper>
  );
}

const Wrapper = styled.div`
  cursor: pointer;

  &:hover {
    filter: brightness(0.9);
  }
`;

export default AddFavorite;

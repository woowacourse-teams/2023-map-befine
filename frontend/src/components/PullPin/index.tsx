import { styled } from 'styled-components';

import { TagProps } from '../../types/Tag';
import Button from '../common/Button';
import Flex from '../common/Flex';
import Space from '../common/Space';
import Tag from '../Tag';

export interface MergeOrSeeTogetherProps {
  tags: TagProps[];
  confirmButton: string;
  onClickConfirm: () => void;
  onClickClose: () => void;
}

function PullPin({
  tags,
  confirmButton,
  onClickConfirm,
  onClickClose,
}: MergeOrSeeTogetherProps) {
  if (tags.length === 0) return null;

  return (
    <Wrapper>
      <Space size={6} />
      <Flex
        $flexDirection="row"
        $justifyContent="center"
        $flexWrap="wrap"
        $gap="12px 12px"
      >
        {tags.length > 5 ? (
          <>
            <Tag tabIndex={1}>{tags[0].title}</Tag>
            <Tag tabIndex={2}>{tags[1].title}</Tag>
            <Tag tabIndex={3}>{tags[2].title}</Tag>
            <Tag tabIndex={4}>{tags[3].title}</Tag>
            <Tag tabIndex={5}>{tags[4].title}</Tag>
            <Tag tabIndex={6}>외 {String(tags.length - 5)}개</Tag>
          </>
        ) : (
          tags.map((tag) => (
            <Tag
              key={`${tag.id}-${tag.title}`}
              tabIndex={1}
              aria-label={
                confirmButton === '같이보기'
                  ? `선택된 ${tag.title} 토픽 태그`
                  : `선택된 ${tag.title} 핀 태그`
              }
            >
              {tag.title}
            </Tag>
          ))
        )}
      </Flex>

      <Space size={6} />

      <Flex $flexDirection="row" $alignItems="center" $justifyContent="center">
        <Button
          variant="secondary"
          onClick={onClickClose}
          aria-label={
            confirmButton === '같이보기'
              ? '선택된 토픽들 같이보기 취소하기'
              : '선택된 핀들 뽑아오기 취소하기'
          }
        >
          취소하기
        </Button>
        <Space size={2} />
        <Button
          variant="primary"
          onClick={onClickConfirm}
          aria-label={
            confirmButton === '같이보기'
              ? '선택된 토픽들 같이보기'
              : '선택된 핀들 뽑아오기'
          }
        >
          {confirmButton}
        </Button>
      </Flex>
      <Space size={4} />
    </Wrapper>
  );
}

const Wrapper = styled.section`
  width: 332px;
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  background-color: white;
  position: fixed;
  border-radius: ${({ theme }) => theme.radius.small};
  z-index: 1;
  border-bottom: 4px solid ${({ theme }) => theme.color.black};

  @media (max-width: 1076px) {
    width: 50vw;
  }

  @media (max-width: 744px) {
    width: 100vw;
  }

  @media (max-width: 372px) {
    width: 332px;
  }
`;

export default PullPin;

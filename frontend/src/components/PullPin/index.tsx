import { styled } from 'styled-components';
import Button from '../common/Button';
import Flex from '../common/Flex';
import Space from '../common/Space';
import Tag from '../Tag';

export interface MergeOrSeeTogetherProps {
  tag: string[];
  confirmButton: string;
  onClickConfirm: () => void;
  onClickClose: () => void;
}

const PullPin = ({
  tag,
  confirmButton,
  onClickConfirm,
  onClickClose,
}: MergeOrSeeTogetherProps) => {
  if (tag.length === 0) return <></>;

  return (
    <Wrapper
      width="332px"
      $flexDirection="column"
      $alignItems="center"
      $justifyContent="center"
      $backgroundColor="white"
      position="fixed"
      $borderRadius="small"
      $zIndex={1}
    >
      <Flex
        $flexDirection="row"
        $justifyContent="left"
        $flexWrap="wrap"
        $gap="12px 12px"
      >
        {tag.length > 2 ? (
          <>
            <Tag tabIndex={1}>{tag[0]}</Tag>
            <Tag tabIndex={2}>{tag[1]}</Tag>
            <Tag tabIndex={3}>외 {String(tag.length - 2)}개</Tag>
          </>
        ) : (
          tag.map((title, index) => (
            <Tag
              key={`${index}-${title}`}
              tabIndex={1}
              aria-label={
                confirmButton === '같이보기'
                  ? `선택된 ${title} 토픽 태그`
                  : `선택된 ${title} 핀 태그`
              }
            >
              {title}
            </Tag>
          ))
        )}
      </Flex>

      <Space size={4} />

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
};

const Wrapper = styled(Flex)`
  border-bottom: 2px solid ${({ theme }) => theme.color.black};
`;

export default PullPin;
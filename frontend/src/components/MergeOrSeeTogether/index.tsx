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

export const MergeOrSeeTogether = ({
  tag,
  confirmButton,
  onClickConfirm,
  onClickClose,
}: MergeOrSeeTogetherProps) => {
  if (tag.length === 0) return <></>;

  return (
    <>
      <Flex
        width="360px"
        $flexDirection="column"
        $alignItems="center"
        $justifyContent="center"
        $backgroundColor="white"
      >
        <Flex
          width="360px"
          $flexDirection="row"
          $justifyContent="left"
          $flexWrap="wrap"
          $gap="12px 12px"
        >
          {tag.map((title, index) => (
            <Tag key={index}>{title}</Tag>
          ))}
        </Flex>

        <Space size={5} />
        <Flex
          $flexDirection="row"
          $alignItems="center"
          $justifyContent="center"
        >
          <Button variant="secondary" onClick={onClickClose}>
            취소하기
          </Button>
          <Space size={2} />
          <Button variant="primary" onClick={onClickConfirm}>
            {confirmButton}
          </Button>
        </Flex>
      </Flex>
    </>
  );
};

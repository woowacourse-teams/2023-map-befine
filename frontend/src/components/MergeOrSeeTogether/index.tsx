import Button from '../common/Button';
import Flex from '../common/Flex';
import Space from '../common/Space';
import Tag from '../common/Tag';

export interface MergeOrSeeTogetherProps {
  tag: string[];
  confirmButton: string;
}

export const MergeOrSeeTogether = ({
  tag,
  confirmButton,
}: MergeOrSeeTogetherProps) => {
  if (!tag || tag.length <= 0) return<></>;

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
          <Button variant="secondary">취소하기</Button>
          <Space size={2} />
          <Button variant="primary">{confirmButton}</Button>
        </Flex>
      </Flex>
    </>
  );
};

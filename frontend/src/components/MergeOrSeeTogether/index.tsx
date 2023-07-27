import Button from '../common/Button';
import Flex from '../common/Flex';
import Space from '../common/Space';
import Tag from '../common/Tag';

export interface MergeOrSeeTogetherProps {
  tag: string[];
  confirmButton: string;
}

export const MergeOrSeeTogether = ({ tag, confirmButton }: MergeOrSeeTogetherProps) => {
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
          {tag && tag.length > 0
            ? tag.map((title, index) => <Tag key={index}>{title}</Tag>)
            : null}
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

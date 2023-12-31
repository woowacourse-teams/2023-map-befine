/* eslint-disable react/require-default-props */
import { useContext, useState } from 'react';
import styled from 'styled-components';

import useGet from '../../apiHooks/useGet';
import { ModalContext } from '../../context/ModalContext';
import {
  TopicAuthorMember,
  TopicAuthorMemberWithAuthorId,
} from '../../types/Topic';
import Box from '../common/Box';
import Button from '../common/Button';
import Checkbox from '../common/CheckBox';
import Flex from '../common/Flex';
import Space from '../common/Space';
import Text from '../common/Text';
import Modal from '../Modal';

interface AuthorityRadioContainerProps {
  isPrivate: boolean;
  isAllPermissioned: boolean;
  authorizedMemberIds: number[];
  setIsPrivate: React.Dispatch<React.SetStateAction<boolean>>;
  setIsAllPermissioned: React.Dispatch<React.SetStateAction<boolean>>;
  setAuthorizedMemberIds: React.Dispatch<React.SetStateAction<number[]>>;
  permissionedMembers?: TopicAuthorMemberWithAuthorId[];
}

function AuthorityRadioContainer({
  isPrivate,
  isAllPermissioned,
  authorizedMemberIds,
  setIsPrivate,
  setIsAllPermissioned,
  setAuthorizedMemberIds,
  permissionedMembers,
}: AuthorityRadioContainerProps) {
  const { openModal, closeModal } = useContext(ModalContext);
  const { fetchGet } = useGet();

  const [members, setMembers] = useState<TopicAuthorMember[]>([]);
  const viewPrevAuthorMembersCondition =
    authorizedMemberIds.length === 0 && !isAllPermissioned;

  const onChangeInitAuthMembers = () => {
    setIsAllPermissioned(false);
    openModal('newTopic');
    setAuthorizedMemberIds([]);

    fetchGet<TopicAuthorMember[]>(
      '/members',
      '사용자 목록을 가져오는데 실패했습니다.',
      (response) => {
        setMembers(response);
      },
    );
  };

  const onChangeInitAuthMembersWithSetIsAllPermissioned = () => {
    setIsAllPermissioned(true);
    setAuthorizedMemberIds([]);
  };

  const onChangeMemberChecked = (isChecked: boolean, id: number) => {
    setAuthorizedMemberIds((prev: TopicAuthorMember['id'][]) =>
      isChecked ? [...prev, id] : prev.filter((n: number) => n !== id),
    );
  };

  return (
    <>
      <Text color="black" $fontSize="default" $fontWeight="normal">
        지도 종류
      </Text>
      <Space size={1} />
      <Flex $alignItems="flex-start">
        <Flex width="108px" $alignItems="flex-start">
          <input
            type="radio"
            id="publicity-public"
            checked={!isPrivate}
            onChange={() => setIsPrivate(false)}
            tabIndex={4}
          />
          <Space size={1} />
          <label htmlFor="publicity-public">공개 지도</label>
        </Flex>

        <Space size={2} />

        <input
          type="radio"
          id="publicity-private"
          checked={isPrivate}
          onChange={() => setIsPrivate(true)}
          tabIndex={4}
        />
        <Space size={1} />
        <label htmlFor="publicity-private">비공개 지도</label>
      </Flex>

      <Space size={5} />
      <Space size={0} />

      <Text color="black" $fontSize="default" $fontWeight="normal">
        핀 생성 및 수정 권한 부여
      </Text>

      <Space size={1} />

      <Flex $alignItems="flex-start">
        <Flex width="108px" $alignItems="flex-start">
          <input
            type="radio"
            id="permission-all"
            checked={isAllPermissioned}
            onChange={onChangeInitAuthMembersWithSetIsAllPermissioned}
            tabIndex={5}
          />
          <Space size={1} />
          {isPrivate ? (
            <label htmlFor="permission-all">혼자만</label>
          ) : (
            <label htmlFor="permission-all">모두에게</label>
          )}
        </Flex>

        <Space size={2} />

        <input
          type="radio"
          id="permission-group"
          checked={!isAllPermissioned}
          onChange={onChangeInitAuthMembers}
          onClick={() => {
            isAllPermissioned === false && openModal('newTopic');
          }}
          tabIndex={5}
        />
        <Space size={1} />
        <label htmlFor="permission-group">친구들에게</label>
      </Flex>

      {authorizedMemberIds.length > 0 && (
        <>
          <Space size={5} />
          <Space size={0} />
          <Box>
            <Text color="black" $fontSize="default" $fontWeight="normal">
              선택한 친구들
            </Text>
            <Space size={1} />
            {members.map((member) => {
              if (authorizedMemberIds.includes(member.id))
                return (
                  <Text
                    color="black"
                    $fontSize="default"
                    $fontWeight="normal"
                    key={member.id}
                  >
                    • {member.nickName}
                  </Text>
                );
            })}
          </Box>
        </>
      )}

      {permissionedMembers && viewPrevAuthorMembersCondition && (
        <>
          <Space size={5} />
          <Space size={0} />
          <Box>
            <Text color="black" $fontSize="default" $fontWeight="normal">
              이전에 권한을 부여한 친구들
            </Text>
            <Space size={1} />
            {permissionedMembers.length > 0 ? (
              permissionedMembers.map((member) => (
                <Text
                  color="black"
                  $fontSize="default"
                  $fontWeight="normal"
                  key={member.id}
                >
                  • {member.memberResponse.nickName}
                </Text>
              ))
            ) : (
              <Text color="black" $fontSize="default" $fontWeight="normal">
                • 없음
              </Text>
            )}
          </Box>
        </>
      )}

      <Modal
        modalKey="newTopic"
        position="center"
        width="400px"
        height="600px"
        overflow="scroll"
        $dimmedColor="rgba(0, 0, 0, 0.5)"
      >
        <ModalContentsWrapper>
          <Flex
            padding="12px"
            position="sticky"
            top="0"
            $justifyContent="space-between"
            $alignItems="center"
          >
            <Text $fontSize="large" $fontWeight="bold" color="black">
              멤버 선택
            </Text>
            <Text $fontSize="small" $fontWeight="normal" color="black">
              {authorizedMemberIds.length}명 선택됨
            </Text>
          </Flex>
          <Space size={2} />
          <CheckboxList>
            {members.map((member) => (
              <CheckboxListItem key={member.id}>
                <Checkbox
                  id={member.id}
                  isAlreadyChecked={authorizedMemberIds.includes(member.id)}
                  label={member.nickName}
                  onChecked={onChangeMemberChecked}
                />
              </CheckboxListItem>
            ))}
          </CheckboxList>

          <Space size={1} />

          <Flex $justifyContent="end" padding="12px" bottom="0px">
            <Button
              tabIndex={6}
              type="button"
              variant="secondary"
              onClick={() => {
                closeModal('newTopic');
                setIsAllPermissioned(true);
                setAuthorizedMemberIds([]);
              }}
            >
              취소하기
            </Button>

            <Space size={3} />

            <Button
              tabIndex={6}
              variant="primary"
              onClick={() => {
                closeModal('newTopic');
              }}
            >
              선택하기
            </Button>
          </Flex>
          <Space size={2} />
        </ModalContentsWrapper>
      </Modal>
    </>
  );
}

const ModalContentsWrapper = styled.div`
  width: 100%;
  height: 100%;
  background-color: white;
  display: flex;
  flex-direction: column;
`;

const CheckboxList = styled.div`
  flex: 1;
  overflow-y: scroll;
`;

const CheckboxListItem = styled.div`
  box-shadow: 0 4px 6px rgba(0, 0, 0, 0.1);
  margin-bottom: 1rem;
  padding: 1rem;
  border-radius: 5px;
  background-color: white;

  &:last-child {
    margin-bottom: 0;
    border-bottom: none;
  }

  &:hover {
    background-color: #f8f9fa;
  }
`;

export default AuthorityRadioContainer;

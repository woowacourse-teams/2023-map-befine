import { styled } from 'styled-components';

import usePatch from '../../apiHooks/usePatch';
import useToast from '../../hooks/useToast';
import { ProfileProps } from '../../types/Profile';
import Box from '../common/Box';
import Button from '../common/Button';
import Flex from '../common/Flex';
import Space from '../common/Space';
import Text from '../common/Text';

interface UpdateMyInfoProps {
  myInfo: ProfileProps;
  setIsModifyMyInfo: React.Dispatch<React.SetStateAction<boolean>>;
  setMyInfo: React.Dispatch<React.SetStateAction<ProfileProps>>;
}

function UpdateMyInfo({
  myInfo,
  setIsModifyMyInfo,
  setMyInfo,
}: UpdateMyInfoProps) {
  const { fetchPatch } = usePatch();
  const { showToast } = useToast();

  const onChangeMyInfoName = (e: React.ChangeEvent<HTMLInputElement>) => {
    if (e.target.value.length >= 20) return;
    setMyInfo({ ...myInfo, nickName: e.target.value });
  };

  const onClickModifyButton = async () => {
    await fetchPatch({
      url: '/members/my/profiles',
      payload: {
        nickName: myInfo.nickName,
      },
      errorMessage: '회원정보 수정에 실패했습니다.',
      onSuccess: () => {
        localStorage.setItem('user', JSON.stringify(myInfo));
        setIsModifyMyInfo(false);
        showToast('info', '회원정보를 수정했습니다.');
      },
    });
  };

  return (
    <MyInfoContainer
      width="440px"
      height="140px"
      $borderRadius="medium"
      $justifyContent="center"
      $alignItems="center"
    >
      <MyInfoImg src={myInfo.imageUrl} alt="내 프로필 이미지" />
      <Space size={5} />
      <Box>
        <MyInfoInput value={myInfo.nickName} onChange={onChangeMyInfoName} />
        <Space size={0} />
        <Text color="black" $fontSize="small" $fontWeight="normal">
          {myInfo.email}
        </Text>
      </Box>
      <Space size={3} />
      <Button variant="primary" onClick={onClickModifyButton}>
        확인
      </Button>
    </MyInfoContainer>
  );
}

const MyInfoContainer = styled(Flex)`
  border: 1px solid ${({ theme }) => theme.color.lightGray};
`;

const MyInfoImg = styled.img`
  width: 80px;
  height: 80px;

  border-radius: 50%;
`;

const MyInfoInput = styled.input`
  width: 150px;
  height: 35px;
  font-size: ${({ theme }) => theme.fontSize.default};
`;

export default UpdateMyInfo;

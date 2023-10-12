import { SyntheticEvent, useState } from 'react';
import { styled } from 'styled-components';

import Setting from '../../assets/updateBtn.svg';
import { DEFAULT_PROD_URL, DEFAULT_PROFILE_IMAGE } from '../../constants';
import useToast from '../../hooks/useToast';
import { ProfileProps } from '../../types/Profile';
import Box from '../common/Box';
import Button from '../common/Button';
import Flex from '../common/Flex';
import Image from '../common/Image';
import Space from '../common/Space';
import Text from '../common/Text';
import UpdateMyInfo from './UpdateMyInfo';

const accessToken = localStorage.getItem('userToken');

function MyInfo() {
  const { showToast } = useToast();

  const user = JSON.parse(localStorage.getItem('user') || '{}');
  const [isModifyMyInfo, setIsModifyMyInfo] = useState<boolean>(false);
  const [myInfo, setMyInfo] = useState<ProfileProps>({
    nickName: user.nickName,
    email: user.email,
    imageUrl: user.imageUrl,
  });

  const onClickSetting = () => {
    setIsModifyMyInfo(true);
  };

  const onClickLogout = async () => {
    try {
      fetch(`${DEFAULT_PROD_URL}/logout`, {
        method: 'POST',
        headers: {
          'Content-Type': 'application/json',
          Authorization: `Bearer ${accessToken}`,
        },
        body: JSON.stringify({
          accessToken,
        }),
      });

      localStorage.removeItem('user');
      localStorage.removeItem('userToken');
      window.location.href = '/';
      showToast('info', '로그아웃 되었습니다.');
    } catch {
      showToast('error', '로그아웃에 실패했습니다');
    }
  };

  if (isModifyMyInfo) {
    return (
      <UpdateMyInfo
        myInfo={myInfo}
        setIsModifyMyInfo={setIsModifyMyInfo}
        setMyInfo={setMyInfo}
      />
    );
  }

  return (
    <MyInfoContainer
      width="440px"
      height="140px"
      $borderRadius="medium"
      $justifyContent="center"
      $alignItems="center"
    >
      <SettingContainer onClick={onClickSetting}>
        <Setting />
      </SettingContainer>
      <Image
        width="80px"
        height="80px"
        src={user.imageUrl || DEFAULT_PROFILE_IMAGE}
        alt="프로필 이미지"
        $errorDefaultSrc={DEFAULT_PROFILE_IMAGE}
        radius="50%"
      />
      <Space size={5} />
      <Box>
        <Flex $justifyContent="space-between" $alignItems="center">
          <Text color="black" $fontSize="medium" $fontWeight="bold">
            {user.nickName}
          </Text>
          <Button variant="custom" onClick={onClickLogout}>
            로그아웃
          </Button>
        </Flex>
        <Text color="black" $fontSize="small" $fontWeight="normal">
          {user.email}
        </Text>
      </Box>
    </MyInfoContainer>
  );
}

const MyInfoContainer = styled(Flex)`
  position: relative;
  border: 1px solid ${({ theme }) => theme.color.lightGray};
`;

const MyInfoImg = styled.img`
  width: 80px;
  height: 80px;

  border-radius: 50%;
`;

const SettingContainer = styled.div`
  position: absolute;
  top: 10px;
  right: 10px;
  cursor: pointer;
`;

export default MyInfo;

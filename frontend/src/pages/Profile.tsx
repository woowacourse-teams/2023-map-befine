import { useState } from 'react';
import { styled } from 'styled-components';
import Box from '../components/common/Box';
import Flex from '../components/common/Flex';
import Space from '../components/common/Space';
import MyInfo from '../components/MyInfo';
import MyInfoContainer from '../components/MyInfoContainer';
import useNavigator from '../hooks/useNavigator';
import useSetNavbarHighlight from '../hooks/useSetNavbarHighlight';
import useSetLayoutWidth from '../hooks/useSetLayoutWidth';
import { FULLSCREEN } from '../constants';

const Profile = () => {
  const { routePage } = useNavigator();
  const { width: _ } = useSetLayoutWidth(FULLSCREEN);
  const { navbarHighlights: __ } = useSetNavbarHighlight('profile');

  const goToPopularTopics = () => {
    routePage('/see-all/popularity');
  };

  return (
    <ProfileWrapper>
      <MyInfoWrapper $justifyContent="center" $alignItems="center">
        <MyInfo />
      </MyInfoWrapper>
      <Space size={6} />
      <MyInfoContainer
        containerTitle="나의 지도"
        containerDescription="내가 만든 지도를 확인해보세요"
      />
    </ProfileWrapper>
  );
};

const ProfileWrapper = styled(Box)`
  width: 1036px;
  margin: 0 auto;
`;

const MyInfoWrapper = styled(Flex)`
  margin-top: ${({ theme }) => theme.spacing[6]};
`;
export default Profile;

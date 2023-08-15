import { useState } from 'react';
import { styled } from 'styled-components';
import Box from '../components/common/Box';
import Flex from '../components/common/Flex';
import Space from '../components/common/Space';
import MyInfo from '../components/MyInfo';
import MyInfoContainer from '../components/MyInfoContainer';
import useNavigator from '../hooks/useNavigator';
import useSetNavbarHighlight from '../hooks/useSetNavbarHighlight';

const MyInfoPage = () => {
  const { routePage } = useNavigator();
  const { navbarHighlights: __ } = useSetNavbarHighlight('profile');

  const goToPopularTopics = () => {
    routePage('/see-all/popularity');
  };

  return (
    <MyInfoPageWrapper>
      <MyInfoWrapper $justifyContent="center" $alignItems="center">
        <MyInfo />
      </MyInfoWrapper>
      <Space size={6} />
      <MyInfoContainer
        containerTitle="나의 지도"
        containerDescription="내가 만든 지도를 확인해보세요"
        routeWhenSeeAll={goToPopularTopics}
      />
      <Space size={6} />
      <MyInfoContainer
        containerTitle="나의 핀"
        containerDescription="내가 표시한 핀을 확인해보세요"
        routeWhenSeeAll={goToPopularTopics}
      />
    </MyInfoPageWrapper>
  );
};

const MyInfoPageWrapper = styled(Box)`
  width: 70vw;
  margin: 0 auto;
`;

const MyInfoWrapper = styled(Flex)`
  margin-top: ${({ theme }) => theme.spacing[6]};
`;
export default MyInfoPage;

import { styled } from 'styled-components';
import Text from '../Text';

export interface TopicCardProps {
  topicImoge: string;
  topicTitle: string;
  topicInformation: string;
}

const TopicCard = ({
  topicImoge,
  topicTitle,
  topicInformation,
}: TopicCardProps) => {
  return (
    <TopicCardContainer>
      <TopicCardButton></TopicCardButton>
      <Text color="black" fontSize="extraLarge" fontWeight="normal">
        {topicImoge}
      </Text>
      <Text color="black" fontSize="medium" fontWeight="normal">
        {topicTitle}
      </Text>
      <Text color="black" fontSize="small" fontWeight="normal">
        {topicInformation}
      </Text>
    </TopicCardContainer>
  );
};

const TopicCardContainer = styled.div`
  width: 360px;
  height: 140px;

  margin-left: 50px;

  position: relative;

  display: flex;
  flex-direction: column;
  justify-content: center;
  align-items: center;

  background-color: ${({ theme }) => theme.color.whiteGray};
  box-shadow: 1px 4px 2px 1px ${({ theme }) => theme.color.lightGray};
`;

const TopicCardButton = styled.button`
  width: 16px;
  height: 16px;

  position: absolute;
  top: 8px;
  right: 8px;

  background-color: ${({ theme }) => theme.color.white};
  border: 1px solid ${({ theme }) => theme.color.black};
  border-radius: ${({ theme }) => theme.radius.medium};

  &:focus {
    background-color: ${({ theme }) => theme.color.primary};
  }
`;

export default TopicCard;

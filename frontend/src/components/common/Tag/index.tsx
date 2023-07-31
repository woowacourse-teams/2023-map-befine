import { styled } from 'styled-components';

const Tag = styled.span`
  height: 28px;
  max-width: 240px;

  padding: ${({ theme }) => `${theme.spacing['1']} ${theme.spacing['3']}`};

  color: ${({ theme }) => theme.color.white};
  font-size: ${({ theme }) => theme.fontSize.extraSmall};

  background-color: ${({ theme }) => theme.color.black};
  border-radius: ${({ theme }) => theme.radius.medium};
`;

export default Tag;

import { styled } from 'styled-components';

const Tag = styled.span`
  display: block;
  height: 32px;
  max-width: 280px;

  padding: ${({ theme }) => `${theme.spacing['1']} ${theme.spacing['3']}`};

  color: ${({ theme }) => theme.color.white};
  font-size: ${({ theme }) => theme.fontSize.small};

  background-color: ${({ theme }) => theme.color.black};
  border-radius: ${({ theme }) => theme.radius.medium};
`;

export default Tag;

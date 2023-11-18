import { useState } from 'react';
import styled from 'styled-components';

import SearchIcon from '../../assets/search.svg';
import useNavigator from '../../hooks/useNavigator';

function SearchBar() {
  const { routingHandlers } = useNavigator();

  const [searchTerm, setSearchTerm] = useState('');

  const onInputChange = (e: React.ChangeEvent<HTMLInputElement>) => {
    setSearchTerm(e.target.value);
  };

  const onSubmit = (e: React.FormEvent<HTMLFormElement>) => {
    e.preventDefault();
    routingHandlers.search(searchTerm);
  };

  return (
    <SearchBarWrapper onSubmit={onSubmit}>
      <StyledSearchIcon />
      <SearchInput
        type="text"
        placeholder="관심있는 키워드를 입력하세요"
        onChange={onInputChange}
      />
    </SearchBarWrapper>
  );
}
export default SearchBar;

const SearchBarWrapper = styled.form`
  display: flex;
  padding-left: 20px;
  position: relative;
  border-radius: 5px;
  border: 1px solid #ccc;
  box-shadow: 0px 1px 5px 3px rgba(0, 0, 0, 0.12);
`;

const SearchInput = styled.input`
  height: 45px;
  width: 100%;
  outline: none;
  border: none;
  border-radius: 5px;
  padding: 0 60px 0 20px;
  font-size: 18px;
  font-weight: 500;
  &:focus {
    outline: none !important;
    box-shadow:
      inset -1px -1px rgba(0, 0, 0, 0.075),
      inset -1px -1px rgba(0, 0, 0, 0.075),
      inset -3px -3px rgba(255, 255, 255, 0.6),
      inset -4px -4px rgba(255, 255, 255, 0.6),
      inset rgba(255, 255, 255, 0.6),
      inset rgba(64, 64, 64, 0.15);
  }
`;

const StyledSearchIcon = styled(SearchIcon)`
  position: absolute;
  top: 50%;
  left: 10px;
  transform: translateY(-50%);
  width: 20px;
  height: 20px;
  fill: #ccc;
`;

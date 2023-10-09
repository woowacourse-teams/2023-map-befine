/* eslint-disable react/function-component-definition */
import { memo, useEffect, useState } from 'react';
import styled from 'styled-components';

import { getPoiApi } from '../../../apis/getPoiApi';
import { Poi } from '../../../types/Poi';
import Input from '.';

interface AutocompleteProps {
  defaultValue?: string;
  onSuggestionSelected: (suggestion: Poi) => void;
}

const Autocomplete = ({
  defaultValue,
  onSuggestionSelected,
}: AutocompleteProps) => {
  const [inputValue, setInputValue] = useState<string | undefined>(
    defaultValue,
  );
  const [suggestions, setSuggestions] = useState<Poi[]>([]);
  const [selectedSuggestion, setSelectedSuggestion] = useState<
    Poi['name'] | null
  >(null);

  const onInputChange = (e: React.ChangeEvent<HTMLInputElement>) => {
    if (e.target.value.trim() === '') {
      setSuggestions([]);
      setInputValue('');
      return;
    }
    setInputValue(e.target.value);
  };

  const onClickSuggestion = (suggestion: Poi) => {
    const { name } = suggestion;
    setInputValue(name);
    setSelectedSuggestion(name);
    onSuggestionSelected(suggestion);
  };

  async function fetchData() {
    try {
      const fetchedSuggestions = await getPoiApi(inputValue || '');
      console.log(fetchedSuggestions);
      if (!fetchedSuggestions)
        throw new Error('추천 검색어를 불러오지 못했습니다.');
      setSuggestions(fetchedSuggestions.searchPoiInfo.pois.poi);
    } catch (error) {
      setSuggestions([]);
      console.error(error);
    }
  }

  useEffect(() => {
    if (!inputValue) return;

    fetchData();
  }, [inputValue]);

  useEffect(() => {
    setInputValue(defaultValue);
  }, [defaultValue]);

  return (
    <>
      <AutocompleteInput
        type="text"
        value={inputValue}
        onChange={onInputChange}
        placeholder="장소를 직접 입력하거나 지도에서 클릭하세요."
        onClick={() => setSelectedSuggestion(null)}
      />

      {!selectedSuggestion && (
        <SuggestionsList>
          {suggestions?.map((suggestion: Poi, index: number) => (
            <SuggestionItem
              key={index}
              onClick={() => {
                onClickSuggestion(suggestion);
              }}
            >
              {suggestion.name}
              <Address>
                {suggestion.upperAddrName} {suggestion.middleAddrName}{' '}
                {suggestion.roadName}
              </Address>
              <Description>{suggestion.desc}</Description>
            </SuggestionItem>
          ))}
        </SuggestionsList>
      )}
    </>
  );
};

export default memo(Autocomplete);

const AutocompleteInput = styled(Input)`
  width: 100%;
`;

const SuggestionsList = styled.ul`
  border: 1px solid #ccc;
  border-radius: 4px;
  box-shadow: 0px 4px 5px -2px rgba(0, 0, 0, 0.3);
`;

const SuggestionItem = styled.li`
  padding: ${({ theme }) => theme.spacing['2']};
  cursor: pointer;

  &:hover {
    background-color: #f7f7f7;
  }
`;

const Address = styled.div`
  font-size: ${({ theme }) => theme.fontSize.small};
  color: ${({ theme }) => theme.color.gray};
`;

const Description = styled.div`
  white-space: nowrap;
  overflow: hidden;
  text-overflow: ellipsis;
`;

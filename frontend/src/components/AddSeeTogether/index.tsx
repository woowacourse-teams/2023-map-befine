import { styled } from 'styled-components';
import { postApi } from '../../apis/postApi';
import useToast from '../../hooks/useToast';
import { useState } from 'react';

interface AddSeeTogetherProps {
  id: number;
  children: React.ReactNode;
}

const AddSeeTogether = ({ id, children }: AddSeeTogetherProps) => {
  const { showToast } = useToast();

  const addSeeTogetherList = async (e: React.MouseEvent<HTMLDivElement>) => {
    e.stopPropagation();

    // TODO : post 후 전역 see together List 에 담기, toast 메세지 수정
    // await postApi('',{});
    // await getApi('');

    showToast('info', '준비중인 기능입니다.');
  };

  return <Wrapper onClick={addSeeTogetherList}>{children}</Wrapper>;
};

const Wrapper = styled.div`
  cursor: pointer;
`;

export default AddSeeTogether;

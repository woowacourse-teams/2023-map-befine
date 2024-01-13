import { Fragment, useEffect, useState } from 'react';
import { styled } from 'styled-components';

import useGet from '../../apiHooks/useGet';
import { TopicCardProps } from '../../types/Topic';
import Button from '../common/Button';
import Flex from '../common/Flex';
import Grid from '../common/Grid';
import Space from '../common/Space';
import Text from '../common/Text';
import TopicCard from '../TopicCard';

interface TopicCardListProps {
  url: string;
  errorMessage: string;
  commentWhenEmpty: string;
  pageCommentWhenEmpty: string;
  routePage: () => void;
  children?: React.ReactNode;
}

function TopicCardList({
  url,
  errorMessage,
  commentWhenEmpty,
  pageCommentWhenEmpty,
  routePage,
  children,
}: TopicCardListProps) {}

export default TopicCardList;

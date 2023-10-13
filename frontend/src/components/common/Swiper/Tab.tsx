import React from 'react';

interface Props {
  label: string;
  children?: React.ReactNode;
}

function Tab({ children }: Props) {
  return <div>{children}</div>;
}

export default Tab;

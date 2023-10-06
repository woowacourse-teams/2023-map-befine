import { ImgHTMLAttributes } from 'react';
import { styled } from 'styled-components';

interface ImageProps extends ImgHTMLAttributes<HTMLImageElement> {
  $objectFit?: string;
}

const Image = styled.img<ImageProps>`
  display: block;
  width: ${({ width }) => width};
  height: ${({ height }) => height};
  object-fit: ${({ $objectFit }) => $objectFit};
`;

export default Image;

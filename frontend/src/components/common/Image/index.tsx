import { ImgHTMLAttributes, SyntheticEvent } from 'react';
import { styled } from 'styled-components';

interface ImageProps extends ImgHTMLAttributes<HTMLImageElement> {
  width?: string;
  height?: string;
  $errorDefaultSrc?: string;
  $objectFit?: string;
  radius?: 'small' | 'medium' | '50%';
  ratio?: string;
}

export default function Image({
  width,
  height,
  src,
  alt,
  $objectFit = 'cover',
  $errorDefaultSrc,
  radius,
  ratio,
}: ImageProps) {
  return (
    <StyledImage
      width={width}
      height={height}
      src={src}
      alt={alt}
      $objectFit={$objectFit}
      radius={radius}
      ratio={ratio}
      onError={(e: SyntheticEvent<HTMLImageElement, Event>) => {
        if ($errorDefaultSrc) e.currentTarget.src = $errorDefaultSrc;
      }}
    />
  );
}

const StyledImage = styled.img<Omit<ImageProps, '$errorDefaultSrc'>>`
  display: block;
  width: ${({ width }) => width};
  height: ${({ height }) => height};
  object-fit: ${({ $objectFit }) => $objectFit};
  border-radius: ${({ radius, theme }) =>
    radius && radius === '50%' ? '50%' : theme.radius[`${radius}`]};
  aspect-ratio: ${({ ratio }) => ratio};
`;

import styled from 'styled-components';
import { ImageProps } from '../../types/Pin';
import Image from '../common/Image';

interface PinImageContainerProps {
  images: ImageProps[];
}

const PinImageContainer = ({ images }: PinImageContainerProps) => {
  return (
    <>
      <FilmList>
        {images.map((image, index) => (
          <ImageWrapper>
            <Image
              key={index}
              height="100px"
              width="100px"
              src={image.imageUrl}
            />
          </ImageWrapper>
        ))}
      </FilmList>
    </>
  );
};

const FilmList = styled.ul`
  width: 330px;
  display: flex;
  flex-direction: row;

  overflow: hidden;
`;

const ImageWrapper = styled.li`
  margin-right: 10px;
`;

export default PinImageContainer;

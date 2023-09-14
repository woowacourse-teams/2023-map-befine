import styled from 'styled-components';
import { ImagesType } from '../../types/Pin';
import Image from '../common/Image';

interface PinImageBoxProps {
  images: ImagesType[];
}

const PinImageBox = ({ images }: PinImageBoxProps) => {
  return (
    <>
      <FilmList>
        {images.map((image, index) => (
          <ImageLi>
            <Image
              key={index}
              height="100px"
              width="100px"
              src={image.imageUrl}
            />
          </ImageLi>
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

const ImageLi = styled.li`
  margin-right: 10px;
`;

export default PinImageBox;

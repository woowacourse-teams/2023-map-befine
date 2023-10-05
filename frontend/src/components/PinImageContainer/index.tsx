import styled from 'styled-components';

import { ImageProps } from '../../types/Pin';
import Image from '../common/Image';
import RemoveImageButton from '../../assets/remove_image_icon.svg';
import useDelete from '../../apiHooks/useDelete';
import useToast from '../../hooks/useToast';

interface PinImageContainerProps {
  images: ImageProps[];
}

const NOT_FOUND_IMAGE =
  'https://dr702blqc4x5d.cloudfront.net/2023-map-be-fine/icon/notFound_image.svg';

const PinImageContainer = ({ images }: PinImageContainerProps) => {
  const { fetchDelete } = useDelete();
  const { showToast } = useToast();

  const onRemovePinIcon = (imageId: number) => {
    const isRemoveImage = confirm('해당 이미지를 삭제하시겠습니까?');

    if (isRemoveImage) {
      fetchDelete({
        url: `/pins/images/${imageId}`,
        errorMessage: '이미지 제거에 실패했습니다.',
        isThrow: true,
        onSuccess: () => {
          showToast('info', '핀에서 이미지가 삭제 되었습니다.');
        },
      });
    }
  };
  return (
    <>
      <FilmList>
        {images.map(
          (image, index) =>
            index < 3 && (
              <ImageWrapper>
                <Image
                  key={image.id}
                  height="100px"
                  width="100px"
                  src={image.imageUrl}
                  $errorDefaultSrc={NOT_FOUND_IMAGE}
                />
                <RemoveImageIconWrapper
                  onClick={() => onRemovePinIcon(image.id)}
                >
                  <RemoveImageButton />
                </RemoveImageIconWrapper>
              </ImageWrapper>
            ),
        )}
      </FilmList>
    </>
  );
};

const FilmList = styled.ul`
  width: 330px;
  display: flex;
  flex-direction: row;
`;

const ImageWrapper = styled.li`
  position: relative;
  margin-right: 10px;
`;

const RemoveImageIconWrapper = styled.div`
  opacity: 0.6;
  position: absolute;
  right: 1px;
  top: 1px;
  line-height: 0;
  background-color: #ffffff;
  cursor: pointer;

  &:hover {
    opacity: 1;
  }
`;

export default PinImageContainer;

import { Swiper, Tab } from 'map-befine-swiper';
import { useState } from 'react';
import styled from 'styled-components';

import useDelete from '../../apiHooks/useDelete';
import RemoveImageButton from '../../assets/remove_image_icon.svg';
import { ImageModal, useModalContext } from '../../context/ImageModalContext';
import useToast from '../../hooks/useToast';
import { ImageProps } from '../../types/Pin';
import Button from '../common/Button';
import Image from '../common/Image';
import Space from '../common/Space';

interface PinImageContainerProps {
  images: ImageProps[];
  getPinData: () => void;
}
const NOT_FOUND_IMAGE =
  'https://dr702blqc4x5d.cloudfront.net/2023-map-be-fine/icon/notFound_image.svg';

function PinImageContainer({ images, getPinData }: PinImageContainerProps) {
  const { fetchDelete } = useDelete();
  const { showToast } = useToast();
  const { isModalOpen, openModal, closeModal } = useModalContext();
  const [modalImage, setModalImage] = useState<string>('');

  const onRemovePinImage = (imageId: number) => {
    const isRemoveImage = confirm('해당 이미지를 삭제하시겠습니까?');

    if (isRemoveImage) {
      fetchDelete({
        url: `/pins/images/${imageId}`,
        errorMessage: '이미지 제거에 실패했습니다.',
        onSuccess: () => {
          showToast('info', '핀에서 이미지가 삭제 되었습니다.');
          getPinData();
        },
      });
    }
  };

  const onImageOpen = (imageUrl: string) => {
    setModalImage(imageUrl);
    openModal();
  };

  if (images.length <= 3) {
    return (
      <FilmList>
        {images.map((image, index) => (
          <Tab label={`${index}`} key={image.id}>
            <ImageWrapper key={`image-${index}`}>
              <div onClick={() => onImageOpen(image.imageUrl)}>
                <Image
                  key={image.id}
                  height="100px"
                  width="100px"
                  src={image.imageUrl}
                  $errorDefaultSrc={NOT_FOUND_IMAGE}
                />
              </div>
              <RemoveImageIconWrapper
                onClick={() => onRemovePinImage(image.id)}
              >
                <RemoveImageButton />
              </RemoveImageIconWrapper>
            </ImageWrapper>
          </Tab>
        ))}
        {isModalOpen && (
          <ImageModal closeModalHandler={closeModal}>
            <ModalImageWrapper>
              <ModalImage src={modalImage} />
              <Space size={3} />
              <Button variant="custom" onClick={closeModal}>
                닫기
              </Button>
            </ModalImageWrapper>
          </ImageModal>
        )}
      </FilmList>
    );
  }

  return (
    <Wrapper>
      <Swiper
        as="ul"
        width={330}
        height={100}
        $elementsOneTab={3}
        swiper
        swipeable
        $isNotTabBoxShow
      >
        {images.map((image, index) => (
          <Tab label={`${index}`} key={image.id}>
            <ImageWrapper key={`image-${index}`}>
              <div onClick={() => onImageOpen(image.imageUrl)}>
                <Image
                  key={image.id}
                  height="100px"
                  width="100px"
                  src={image.imageUrl}
                  $errorDefaultSrc={NOT_FOUND_IMAGE}
                />
              </div>
              <RemoveImageIconWrapper
                onClick={() => onRemovePinImage(image.id)}
              >
                <RemoveImageButton />
              </RemoveImageIconWrapper>
            </ImageWrapper>
          </Tab>
        ))}
        {isModalOpen && (
          <ImageModal closeModalHandler={closeModal}>
            <ModalImageWrapper>
              <ModalImage src={modalImage} alt="선택한 장소 이미지" />
              <Space size={3} />
              <Button variant="custom" onClick={closeModal}>
                닫기
              </Button>
            </ModalImageWrapper>
          </ImageModal>
        )}
      </Swiper>
    </Wrapper>
  );
}

const Wrapper = styled.div`
  width: 330px;
`;

const FilmList = styled.ul`
  display: flex;
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

const ModalImageWrapper = styled.div`
  width: 100%;
  height: 100%;

  display: flex;
  flex-direction: column;
  align-items: center;

  margin: 0 auto;
  overflow: hidden;
`;

const ModalImage = styled.img`
  width: 100%;
  height: 100%;

  min-width: 360px;
  min-height: 360px;

  display: block;
`;
export default PinImageContainer;

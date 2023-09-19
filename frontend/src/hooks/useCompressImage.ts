import imageCompression from 'browser-image-compression';

const useCompressImage = () => {
  const compressImage = async (file: File) => {
    const resizingBlob = await imageCompression(file, {
      maxSizeMB: 1,
      maxWidthOrHeight: 500,
      useWebWorker: true,
    });
    const resizingFile = new File([resizingBlob], file.name, {
      type: file.type,
    });
    return resizingFile;
  };

  const compressImageList = async (files: FileList) => {
    const compressedImageList: File[] = [];

    for (const file of files) {
      const compressedImage = await compressImage(file);
      compressedImageList.push(compressedImage);
    }

    return compressedImageList;
  };

  return { compressImage, compressImageList };
};

export default useCompressImage;

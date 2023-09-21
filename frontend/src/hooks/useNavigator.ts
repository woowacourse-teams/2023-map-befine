import { useContext } from 'react';
import { useNavigate, useParams } from 'react-router-dom';
import { ModalContext } from '../context/ModalContext';

const useNavigator = () => {
  const navigator = useNavigate();

  const { openModal, closeModal } = useContext(ModalContext);
  const { topicId } = useParams();
  const routePage = (url: string | -1, value?: string | number | number[]) => {
    if (typeof url === 'string') navigator(url, { state: value });
    if (url === -1) navigator(url);
  };

  return {
    routingHandlers: {
      home: () => routePage('/'),
      seeTogether: () => routePage('/see-together'),
      addMapOrPin: () => openModal('addMapOrPin'),
      favorite: () => routePage('/favorite'),
      profile: () => routePage('/my-page'),
      newTopic: () => {
        routePage('/new-topic');
        closeModal('addMapOrPin');
      },
      newPin: () => {
        routePage('/new-pin', topicId);
        closeModal('addMapOrPin');
      },
      goToPopularTopics: () => routePage('see-all/popularity'),
      goToNearByMeTopics: () => routePage('see-all/near'),
      goToLatestTopics: () => routePage('see-all/latest'),
    },
    routePage,
  };
};

export default useNavigator;

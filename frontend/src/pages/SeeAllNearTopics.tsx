import useSetNavbarHighlight from '../hooks/useSetNavbarHighlight';

const SeeAllNearTopics = () => {
  const { navbarHighlights: _ } = useSetNavbarHighlight('home');

  return <div>nearTopics</div>;
};

export default SeeAllNearTopics;

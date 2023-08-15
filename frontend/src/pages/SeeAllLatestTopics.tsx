import useSetNavbarHighlight from '../hooks/useSetNavbarHighlight';

const SeeAllLatestTopics = () => {
  const { navbarHighlights: _ } = useSetNavbarHighlight('home');

  return <div>latestTopics</div>;
};

export default SeeAllLatestTopics;

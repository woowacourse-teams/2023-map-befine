import useSetNavbarHighlight from '../hooks/useSetNavbarHighlight';

const SeeTogetherTopics = () => {
  const { navbarHighlights: _ } = useSetNavbarHighlight('seeTogether');

  return <div>seeTogether</div>;
};

export default SeeTogetherTopics;

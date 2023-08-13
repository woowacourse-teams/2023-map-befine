import useSetNavbarHighlight from '../hooks/useSetNavbarHighlight';

const Favorite = () => {
  const { navbarHighlights: _ } = useSetNavbarHighlight('favorite');

  return <div>favoriteTopics</div>;
};

export default Favorite;

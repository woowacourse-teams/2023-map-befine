import useSetNavbarHighlight from '../hooks/useSetNavbarHighlight';

const Profile = () => {
  const { navbarHighlights: _ } = useSetNavbarHighlight('profile');

  return <div>profile</div>;
};

export default Profile;

import { Header } from '../../components';
import Modal from '../../components/modal';

const Dashboard = () => {
  const hideProfile = true;
  return (
    <>
      <Header hideProfile={hideProfile} />
      <Modal />
    </>
  );
};

export default Dashboard;

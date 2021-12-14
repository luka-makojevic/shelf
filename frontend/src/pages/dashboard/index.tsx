import { useLocation, Outlet } from 'react-router-dom';
import { Header } from '../../components';
import {
  DashboardContentWrapper,
  DashboardLayout,
} from '../../components/layout/layout.styles';
import NavigationSidebar from '../../components/navigationSidebar';
import { Routes } from '../../utils/enums/routes';
import Shelves from '../shelves';

const Dashboard = () => {
  const location = useLocation();
  const isRootDashboard = location.pathname === Routes.DASHBOARD;

  return (
    <>
      <Header />
      <DashboardLayout>
        <NavigationSidebar />
        <DashboardContentWrapper>
          {isRootDashboard ? <Shelves /> : <Outlet />}
        </DashboardContentWrapper>
      </DashboardLayout>
    </>
  );
};

export default Dashboard;

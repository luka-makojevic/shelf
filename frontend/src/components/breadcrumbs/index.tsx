import { Link } from '../text/text-styles';
import { useAppSelector } from '../../store/hooks';
import { BreadcrumbsContainer } from './breadcrumbs-styles';

interface PathHistoryData {
  name: string;
  id: number;
}

const Breadcrumbs = () => {
  // pathHistory will come from file page when connected to backend
  // const pathHistory: PathHistoryData[] = useAppSelector((state) => state.pathHistory.pathHistory);
  const pathHistory = [
    { name: 'new shelf', id: 54 },
    { name: 'folder1', id: 5 },
    { name: 'folder2', id: 6 },
  ];
  const rootUrl = pathHistory[0] && `/dashboard/shelves/${pathHistory[0].id}`;
  const separator = '/';

  return (
    <BreadcrumbsContainer>
      {pathHistory.map((item: PathHistoryData, i: number) => (
        <span>
          {i === 0 ? (
            <span>
              <Link to={rootUrl}>{pathHistory[0].name}</Link>
              {` ${separator} `}
            </span>
          ) : (
            <>
              {i === pathHistory.length - 1 ? (
                <span>{item.name}</span>
              ) : (
                <span>
                  <Link to={`${rootUrl}/folders/${item.id}`}>{item.name}</Link>
                  {` ${separator} `}
                </span>
              )}
            </>
          )}
        </span>
      ))}
    </BreadcrumbsContainer>
  );
};

export default Breadcrumbs;

import { Link } from '../text/text-styles';
import { useAppSelector } from '../../store/hooks';
import { BreadcrumbsContainer } from './breadcrumbs.styles';
import { PathHistoryData } from '../../interfaces/dataTypes';

const Breadcrumbs = () => {
  const pathHistory: PathHistoryData[] = useAppSelector(
    (state) => state.pathHistory.pathHistory
  );

  const rootUrl =
    pathHistory &&
    pathHistory[0] &&
    `/dashboard/shelves/${pathHistory[0].folderId}`;
  const separator = '/';

  return (
    <BreadcrumbsContainer>
      {pathHistory &&
        pathHistory.map((item: PathHistoryData, i: number) => (
          <span key={item.folderId}>
            {i === 0 ? (
              <span>
                <Link to={rootUrl}>{pathHistory[0].folderName}</Link>
                {` ${separator} `}
              </span>
            ) : (
              <>
                {i === pathHistory.length - 1 ? (
                  <span>{item.folderName}</span>
                ) : (
                  <span>
                    <Link to={`${rootUrl}/folders/${item.folderId}`}>
                      {item.folderName}
                    </Link>
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

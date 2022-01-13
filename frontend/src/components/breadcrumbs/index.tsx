import { Link } from '../text/text-styles';
import { PathHistoryData } from '../../interfaces/dataTypes';
import { BreadcrumbsContainer } from './breadcrumbs.styles';

const Breadcrumbs = ({ pathHistory }: { pathHistory: PathHistoryData[] }) => {
  let rootUrl =
    pathHistory &&
    pathHistory[0] &&
    `/dashboard/shelves/${pathHistory[0].folderId}`;
  if (pathHistory && pathHistory[0] && pathHistory[0].folderName === 'trash') {
    rootUrl =
      pathHistory &&
      pathHistory[0] &&
      `/dashboard/${pathHistory[0].folderName}`;
  }
  const separator = '/';

  return (
    <BreadcrumbsContainer>
      {pathHistory &&
        pathHistory.map((item: PathHistoryData, i: number) => (
          <span key={item.folderId}>
            {i === 0 && item.folderName !== 'trash' ? (
              <span>
                {pathHistory.length === 1 ? (
                  <span>{pathHistory[0].folderName}</span>
                ) : (
                  <Link to={rootUrl}>{pathHistory[0].folderName}</Link>
                )}

                {` ${pathHistory.length > 1 ? separator : ''} `}
              </span>
            ) : (
              <>
                {i === pathHistory.length - 1 ? (
                  <span>{item.folderName}</span>
                ) : (
                  <span>
                    {item.folderName !== 'trash' && (
                      <>
                        <Link to={`${rootUrl}/folders/${item.folderId}`}>
                          {item.folderName}
                        </Link>
                        {` ${separator} `}
                      </>
                    )}
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

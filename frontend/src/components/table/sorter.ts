import {
  FileTableDataTypes,
  FunctionTableDataTypes,
  HeaderTypes,
  ShelfTableDataTypes,
  SorterDataTypes,
  SortingDirectionTypes,
} from '../../interfaces/dataTypes';
import { SortingDirection } from '../../utils/enums/table';

const sortData = (
  data: (FunctionTableDataTypes | FileTableDataTypes | ShelfTableDataTypes)[],
  sortKey: string,
  sortingDirection: string
) => {
  data.sort((a: SorterDataTypes, b: SorterDataTypes) => {
    let relevantValueA = a[sortKey];
    let relevantValueB = b[sortKey];

    if (sortKey === 'creation_date') {
      relevantValueA = new Date(a[sortKey]).getTime();
      relevantValueB = new Date(b[sortKey]).getTime();
    }

    if (
      sortingDirection === SortingDirection.UNSORTED ||
      sortingDirection === SortingDirection.ASCENDING
    ) {
      if (relevantValueA < relevantValueB) return -1;
      if (relevantValueA > relevantValueB) return 1;
      return 0;
    }
    if (relevantValueA > relevantValueB) return -1;
    if (relevantValueA < relevantValueB) return 1;
    return 0;
  });
};

const getNextSortingDirection = (sortingDirection: SortingDirection) => {
  if (
    sortingDirection === SortingDirection.UNSORTED ||
    sortingDirection === SortingDirection.ASCENDING
  ) {
    return SortingDirection.DESCENDING;
  }
  return SortingDirection.ASCENDING;
};

export const sortColumn = (
  sortKey: string,
  sortingDirections: SortingDirectionTypes,
  tableData: (
    | FunctionTableDataTypes
    | FileTableDataTypes
    | ShelfTableDataTypes
  )[],
  setTableData: (
    data: (FunctionTableDataTypes | FileTableDataTypes | ShelfTableDataTypes)[]
  ) => void,
  setSortingDirections: (data: SortingDirectionTypes) => void
) => {
  const currentSortingDirection = sortingDirections[sortKey];
  const newTableData = [...tableData];

  sortData(newTableData, sortKey, currentSortingDirection);
  const nextSortingDirection = getNextSortingDirection(currentSortingDirection);
  const newSortingDirections = { ...sortingDirections };
  newSortingDirections[sortKey] = nextSortingDirection;
  setTableData(newTableData);
  setSortingDirections(newSortingDirections);
};

export const createSortingDirectons = (
  headers: HeaderTypes[],
  setSortingDirections: (data: SortingDirectionTypes) => void
) => {
  const ourSortingDirections: SortingDirectionTypes = {};
  headers.forEach((header: HeaderTypes) => {
    ourSortingDirections[header.key] = SortingDirection.UNSORTED;
  });
  setSortingDirections(ourSortingDirections);
};

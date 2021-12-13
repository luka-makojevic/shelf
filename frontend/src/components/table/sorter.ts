import React from 'react';
import { headerTypes } from '../../interfaces/dataTypes';

enum SortingDirection {
  ASCENDING = 'ASCENDING',
  DESCENDING = 'DESCENDING',
  UNSORTED = 'UNSORTED',
}

const sortData = (data: any, sortKey: string, sortingDirection: string) => {
  data.sort((a: any, b: any) => {
    let relevantValueA = a[sortKey];
    let relevantValueB = b[sortKey];

    if (sortKey === 'creationDate') {
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
  sortingDirections: any,
  tableData: any,
  setTableData: any,
  setSortingDirections: any
) => {
  const currentSortingDirection = sortingDirections[sortKey];
  const newData = [...tableData];

  sortData(newData, sortKey, currentSortingDirection);
  const nextSortingDirection = getNextSortingDirection(currentSortingDirection);
  const newSortingDirections = { ...sortingDirections };
  newSortingDirections[sortKey] = nextSortingDirection;
  setTableData(newData);
  setSortingDirections(newSortingDirections);
};

export const createSortingDirectons = (
  headers: headerTypes[],
  setSortingDirections: (data: headerTypes) => void
) => {
  const ourSortingDirections: any = {};
  headers.forEach((header: any) => {
    ourSortingDirections[header.key] = SortingDirection.UNSORTED;
  });
  setSortingDirections(ourSortingDirections);
};

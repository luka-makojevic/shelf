import { useEffect, useState } from 'react';
import TableWrapper from '../../components/table/TableWrapper';
import { useShelf } from '../../hooks/shelfHooks';
import { HeaderTypes } from '../../interfaces/dataTypes';
import { useAppSelector } from '../../store/hooks';

const Shelves = () => {
  const shelves = useAppSelector((state) => state.shelf.shelves);
  // const user = useAppSelector((state) => state.user.user);
  const headers: HeaderTypes[] = [];

  // const { getShelves } = useShelf();

  // const [shelvesForTable, setShelvesForTable] = useState<any>();

  // useEffect(() => {
  //  makeHeaders();
  //   const newShelves = shelves.map((shelf) => ({
  //     name: shelf.name,
  //     id: shelf.id,
  //   }));
  //   setShelvesForTable(newShelves);
  // }, [shelves]);

  const makeHeaders = () => {
    Object.keys(shelves[0]).map((key) => {
      let result = key.replace(/_/g, ' ');
      let header = result.charAt(0).toUpperCase() + result.slice(1);
      headers.push({ header, key });
      return null;
    });
  };

  // useEffect(() => {
  //   getShelves(
  //     { userId: user?.id },
  //     () => {
  //       console.log('success');
  //     },
  //     (err: string) => {
  //       console.log(err, 'error');
  //     }
  //   );
  // }, []);

  return (
    <TableWrapper
      title="Shelves"
      description="Shelves are the fundamental containers for data storage."
    >
      {shelves.length === 0 ? <p>no shelfs</p> : <div>Table</div>}
    </TableWrapper>
  );
};

export default Shelves;

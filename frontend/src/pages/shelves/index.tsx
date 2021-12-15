import { useEffect, useState } from 'react';
import TableWrapper from '../../components/table/TableWrapper';
import { useShelf } from '../../hooks/shelfHooks';
import { useAppSelector } from '../../store/hooks';

const Shelves = () => {
  const shelves = useAppSelector((state) => state.shelf.shelves);
  // const user = useAppSelector((state) => state.user.user);

  // const { getShelves } = useShelf();

  // const [shelvesForTable, setShelvesForTable] = useState<any>();

  // useEffect(() => {
  //   const newShelves = shelves.map((shelf) => ({
  //     name: shelf.name,
  //     id: shelf.id,
  //   }));

  //   setShelvesForTable(newShelves);
  // }, [shelves]);

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

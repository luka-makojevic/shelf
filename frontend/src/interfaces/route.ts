export interface RouteProps {
  path: string;
  element: React.ReactNode;
  children?: RouteProps[];
}

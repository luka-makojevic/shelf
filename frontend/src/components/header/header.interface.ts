export interface ProfileProps {
  hideProfile?: boolean;
}

export interface HeaderStyleProps {
  children?: React.ReactNode;
}

export type HeaderProps = {
  hideProfile?: boolean;
  position?: string;
  showButtons?: boolean;
};

export interface HeaderContainerProps {
  position?: string;
}

export interface ProfilePictureProps {
  imgUrl: string;
}

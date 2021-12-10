export interface AlertStyleProp {
  type: string;
}

export interface AlertPorps {
  type: string;
  title: string;
  message: string | undefined;
  onClose: () => void;
}

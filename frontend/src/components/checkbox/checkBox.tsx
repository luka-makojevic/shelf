import React, { ChangeEventHandler, ForwardedRef, ReactNode } from 'react';
import { Checkbox, CheckboxWrapper, Label } from './checkbox-styles';

type CheckBoxProps = {
  onChange?: ChangeEventHandler;
  name?: string;
  id: string;
  children: ReactNode;
};

const CheckBox = React.forwardRef(
  (
    { children, id, ...restProps }: CheckBoxProps,
    ref: ForwardedRef<HTMLInputElement>
  ) => (
    <CheckboxWrapper>
      <Checkbox id={id} type="checkbox" ref={ref} {...restProps} />
      <Label htmlFor={id}>{children}</Label>
      
    </CheckboxWrapper>
  )
);

export default CheckBox;

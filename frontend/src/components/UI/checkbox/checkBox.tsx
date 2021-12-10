import React, { ForwardedRef } from 'react';
import { Checkbox, CheckboxWrapper, Label } from './checkbox-styles';
import { CheckBoxProps } from './checkbox.interfaces';

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

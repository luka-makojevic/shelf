import React, { ChangeEventHandler, ForwardedRef } from 'react';
import { Checkbox } from './checkbox-styles';
// import { Checkbox, CheckboxWrapper, CheckMark } from './checkbox-styles';

type CheckBoxProps = {
  onChange?: ChangeEventHandler;
  name?: string;
  id?: string;
};

const CheckBox = React.forwardRef(
  ({ ...restProps }: CheckBoxProps, ref: ForwardedRef<HTMLInputElement>) => (
    <Checkbox type="checkbox" ref={ref} {...restProps} />
  )
);

export default CheckBox;

import React, { ChangeEventHandler, ForwardedRef, ReactNode } from 'react';
// import { PlainText } from '../text/text-styles';
import { Checkbox, CheckboxWrapper, Label } from './checkbox-styles';

type CheckBoxProps = {
  onChange?: ChangeEventHandler;
  name?: string;
  id?: string;
  label: string | ReactNode; // it must have a label, but it can either be a simple strign, or some type of element, like we needed for the link and text
};

const CheckBox = React.forwardRef(
  (
    { label, ...restProps }: CheckBoxProps,
    ref: ForwardedRef<HTMLInputElement>
  ) => (
    <CheckboxWrapper>
      <Checkbox type="checkbox" ref={ref} {...restProps} />

      <Label>{label}</Label>
    </CheckboxWrapper>
  )
);

export default CheckBox;

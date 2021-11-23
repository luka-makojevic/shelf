import React, { ChangeEventHandler, ForwardedRef } from 'react';
import { FieldError } from 'react-hook-form';
import { Link } from 'react-router-dom';
import { Checkbox, CheckboxText, CheckboxWrapper } from './checkbox-styles';
import { Routes } from '../../enums/routes';
import { Error } from '../input/input-styles';

type CheckBoxProps = {
  onChange?: ChangeEventHandler;
  error?: FieldError;
  name?: string;
  type?: string;
  label?: string;
  link?: string;
};

const CheckBox = React.forwardRef(
  (
    { error, name, label, link, ...restProps }: CheckBoxProps,
    ref: ForwardedRef<HTMLInputElement>
  ) => (
    <>
      <CheckboxWrapper>
        <Checkbox type="checkbox" name={name} ref={ref} {...restProps} />
        <CheckboxText style={{ fontSize: '0.7em' }}>
          {label && label}
          <Link to={Routes.TERMS} target="_blank">
            {link && link}
          </Link>
        </CheckboxText>
      </CheckboxWrapper>
      <Error>{error?.message}</Error>
    </>
  )
);

export default CheckBox;

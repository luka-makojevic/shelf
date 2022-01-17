import { FieldError, UseFormRegister } from 'react-hook-form';
import { RiArrowDownSFill } from 'react-icons/ri';
import { Error } from '../../text/text-styles';
import { SelectContainer, StyledSelect } from './select-styles';

interface SelectProps {
  optionsData: { value: string; text: string }[];
  selectName: string;
  placeHolder: string;
  register: UseFormRegister<any>;
  error: FieldError;
}

export const Select = ({
  optionsData,
  selectName,
  placeHolder,
  register,
  error,
}: SelectProps) => (
  <SelectContainer>
    <RiArrowDownSFill color="black" />
    <StyledSelect
      {...register(selectName, {
        required: 'Select one option',
      })}
    >
      <option hidden value="">
        {placeHolder}
      </option>
      {optionsData.map((option: { value: string; text: string }) => (
        <option key={option.value} value={option.value}>
          {option.text}
        </option>
      ))}
    </StyledSelect>
    {error && <Error>{error.message}</Error>}
  </SelectContainer>
);

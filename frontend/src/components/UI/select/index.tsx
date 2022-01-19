import { FieldValues, UseFormRegister } from 'react-hook-form';
import { RiArrowDownSFill } from 'react-icons/ri';
import { Error } from '../../text/text-styles';
import { SelectContainer, StyledSelect } from './select-styles';

interface SelectProps {
  optionsData: { value: string; text: string }[];
  selectName: string;
  placeHolder: string;
  register: UseFormRegister<FieldValues>;
  error: { message: string };
  validation?: {
    validate: (value: string) => true | "Can't select same shelfs";
  };
}

export const Select = ({
  optionsData,
  selectName,
  placeHolder,
  register,
  error,
  validation,
}: SelectProps) => {
  let currentValiadtion = {
    required: 'Select one option',
  };
  if (validation) {
    currentValiadtion = {
      required: 'Select one option',
      ...validation,
    };
  }

  return (
    <SelectContainer>
      <RiArrowDownSFill color="black" />
      <StyledSelect {...register(selectName, currentValiadtion)}>
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
};

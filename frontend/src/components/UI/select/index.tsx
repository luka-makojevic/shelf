import { useEffect, useRef, useState } from 'react';
import { FieldValues, UseFormRegister, UseFormSetValue } from 'react-hook-form';
import { RiArrowDownSFill, RiArrowUpSFill } from 'react-icons/ri';
import { ShelvesOptionTypes } from '../../modal/modal.interfaces';
import { Error } from '../../text/text-styles';
import {
  DropDownContainer,
  DropDownHeader,
  DropDownList,
  ListItem,
} from './select-styles';

interface SelectProps {
  optionsData: { value: string; text: string }[];
  selectName: string;
  placeHolder: string;
  register: UseFormRegister<FieldValues>;
  error: { message: string };
  validation?: {
    validate: (value: string) => true | 'Cant select same shelfs';
  };
  setValue?: UseFormSetValue<FieldValues>;
  variant?: string;
}

export const Select = ({
  optionsData,
  selectName,
  setValue,
  placeHolder,
  register,
  error,
  validation,
  variant,
}: SelectProps) => {
  const [isOpen, setIsOpen] = useState(false);
  const toggling = () => setIsOpen(!isOpen);
  const [placeholder, setPlaceholder] = useState(placeHolder);

  const validationRule = { required: 'You must select a role' };

  const ListItemW = ({ option }: { option: ShelvesOptionTypes }) => {
    const handleChooseOption = () => {
      if (setValue)
        setValue(selectName, option.value, { shouldValidate: true });
      setPlaceholder(option.text);
      setIsOpen(false);
    };
    return (
      <ListItem key={option.value} onClick={handleChooseOption}>
        {option.text}
      </ListItem>
    );
  };

  const dropDownRef = useRef<HTMLDivElement>(null);
  const selectRef = useRef<HTMLDivElement>(null);

  useEffect(() => {
    const handleOutsideClick = (event: MouseEvent) => {
      if (
        dropDownRef.current &&
        selectRef.current &&
        event.target instanceof Node &&
        !dropDownRef.current.contains(event.target) &&
        !selectRef.current.contains(event.target)
      ) {
        setIsOpen(false);
      }
    };

    document.addEventListener('mousedown', handleOutsideClick);
    return () => {
      document.removeEventListener('mousedown', handleOutsideClick);
    };
  }, [dropDownRef, selectRef]);

  return (
    <DropDownContainer>
      <input {...register(selectName, validation || validationRule)} />
      <DropDownHeader onClick={toggling} variant={variant} ref={selectRef}>
        {placeholder}
        {!isOpen ? (
          <RiArrowDownSFill size="30px" />
        ) : (
          <RiArrowUpSFill size="30px" />
        )}
      </DropDownHeader>

      {error && !isOpen && <Error>{error.message}</Error>}
      {isOpen && (
        <div style={{ position: 'relative' }}>
          <DropDownList ref={dropDownRef} variant={variant}>
            {optionsData.map((option: { value: string; text: string }) => (
              <ListItemW option={option} />
            ))}
          </DropDownList>
        </div>
      )}
    </DropDownContainer>
  );
};

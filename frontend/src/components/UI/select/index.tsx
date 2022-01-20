import { useEffect, useRef, useState } from 'react';
import {
  FieldValues,
  RegisterOptions,
  UseFormRegister,
  UseFormSetValue,
} from 'react-hook-form';
import { RiArrowDownSFill, RiArrowUpSFill } from 'react-icons/ri';
import { ShelvesOptionTypes } from '../../modal/modal.interfaces';
import { Error } from '../../text/text-styles';
import {
  DropDownContainer,
  DropDownHeader,
  DropDownList,
  DropdownItem,
  DropdownListContainer,
} from './select-styles';

interface SelectProps {
  optionsData: { value: string; text: string }[];
  selectName: string;
  placeHolder: string;
  register: UseFormRegister<FieldValues>;
  error: { message: string };
  validation?: RegisterOptions;
  setValue?: UseFormSetValue<FieldValues>;
  variant?: 'secondary' | undefined;
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

  const ListItem = ({
    option,
    index,
  }: {
    option: ShelvesOptionTypes;
    index: number;
  }) => {
    const handleChooseOption = () => {
      if (setValue)
        setValue(selectName, option.value, { shouldValidate: true });
      setPlaceholder(option.text);
      setIsOpen(false);
    };
    return (
      <DropdownItem key={index} onClick={handleChooseOption}>
        {option.text}
      </DropdownItem>
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
        <DropdownListContainer>
          <DropDownList ref={dropDownRef} variant={variant}>
            {optionsData.map(
              (option: { value: string; text: string }, index: number) => (
                <ListItem option={option} index={index} />
              )
            )}
          </DropDownList>
        </DropdownListContainer>
      )}
    </DropDownContainer>
  );
};

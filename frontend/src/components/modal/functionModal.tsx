import { useState } from 'react';
import ReactDOM from 'react-dom';
import { useForm } from 'react-hook-form';
import { FaRegTimesCircle } from 'react-icons/fa';
import { toast } from 'react-toastify';
import { FunctionForm, FunctionFormInner } from '../form/form-styles';
import { InputField } from '../UI/input/InputField';
import { ModalButtonDivider } from '../layout/layout.styles';
import { Button } from '../UI/button';
import functionService from '../../services/functionService';
import { Error, H2 } from '../text/text-styles';
import {
  Backdrop,
  Close,
  FunctionContainer,
  FunctionHeader,
  FunctionModalContainer,
  HeaderItem,
  RadioLabel,
  RadioContainer,
  RadioInner,
  RadioSubTitle,
  RadioTitle,
} from './modal.styles';
import { theme } from '../../theme';
import { InputSubTitle, InputTitle } from '../UI/input/input-styles';
import { Select } from '../UI/select';
import { FunctionFormData } from '../../interfaces/dataTypes';
import { FunctionModalProps } from './modal.interfaces';
import {
  eventTriggerOptions,
  functionOptions,
  languageOptions,
  logTrigger,
} from '../../utils/fixtures/functionOptions';

const FunctionModal = ({
  onCloseModal,
  data,
  onGetData,
}: FunctionModalProps) => {
  const {
    register,
    handleSubmit,
    reset,
    setValue,
    watch,
    formState: { errors },
  } = useForm();

  const [optionValue, setOptionValue] = useState<string>('scratch');

  const handleCloseModal = () => {
    onCloseModal(false);
  };

  const onSubmit = (formData: FunctionFormData) => {
    if (formData.function === 'backup') {
      formData.eventId = 1;
    }
    functionService
      .createPredefinedFunction(formData)
      .then(() => {
        toast.success('Function created');
        onCloseModal(false);
        onGetData();
      })
      .catch((err) => toast.error(err.response?.data.message));
  };

  const validations = {
    required: 'This field is required',
    maxLength: {
      value: 50,
      message: 'Function name can not be longer than 50 characters',
    },
  };

  const handleChange = ({ target }: React.ChangeEvent<HTMLInputElement>) => {
    setOptionValue(target.value);
    reset();
  };

  const predefinedFunction = watch('function');

  const validateBackupShelf = {
    validate: (value: string) =>
      value !== watch('shelfId') || 'Cant select same shelfs',
  };

  const modal = (
    <Backdrop>
      <FunctionModalContainer>
        <FunctionHeader>
          <HeaderItem>
            <H2>Create Function</H2>
          </HeaderItem>

          <HeaderItem>
            <Close onClick={handleCloseModal}>
              <FaRegTimesCircle
                color={theme.colors.white}
                size={theme.space.lg}
              />
            </Close>
          </HeaderItem>
        </FunctionHeader>
        <FunctionContainer>
          <p>choose one of the following options:</p>
          <RadioContainer>
            <RadioLabel htmlFor="scratch">
              <RadioInner>
                <div>
                  <RadioTitle>Author from scratch</RadioTitle>
                  <RadioSubTitle>Write your own code.</RadioSubTitle>
                </div>

                <input
                  type="radio"
                  id="scratch"
                  name="code"
                  value="scratch"
                  checked={optionValue === 'scratch'}
                  onChange={handleChange}
                />
              </RadioInner>
              <img
                src={`${process.env.PUBLIC_URL}/assets/images/configuration.png`}
                alt="gear with coding symbol"
              />
            </RadioLabel>
            <RadioLabel htmlFor="blueprint">
              <RadioInner>
                <div>
                  <RadioTitle>Use a blueprint</RadioTitle>
                  <RadioSubTitle>
                    Build Shelf Function with sample code and configuration
                    preset for common use cases.
                  </RadioSubTitle>
                </div>

                <input
                  type="radio"
                  id="blueprint"
                  name="code"
                  value="blueprint"
                  checked={optionValue === 'blueprint'}
                  onChange={handleChange}
                />
              </RadioInner>
              <img
                src={`${process.env.PUBLIC_URL}/assets/images/coding.png`}
                alt="code symbol"
              />
            </RadioLabel>
          </RadioContainer>
          <FunctionForm onSubmit={handleSubmit(onSubmit)}>
            <FunctionFormInner>
              {optionValue === 'scratch' ? (
                <>
                  <InputTitle>Function Name</InputTitle>
                  <InputSubTitle>
                    Enter a name that describes the purpose of your function
                  </InputSubTitle>
                  <InputField
                    placeholder="Function name"
                    error={errors.name}
                    {...register('name', validations)}
                  />
                  <Error>{errors?.functionName?.message}</Error>
                  <InputTitle>Runtime</InputTitle>
                  <InputSubTitle>
                    Choose the language to use for writing your function
                  </InputSubTitle>
                  <Select
                    optionsData={languageOptions}
                    selectName="language"
                    error={errors.language}
                    register={register}
                    placeHolder="select language"
                  />
                  <InputTitle>Select event trigger</InputTitle>
                  <InputSubTitle>
                    When should function be executed
                  </InputSubTitle>
                  <Select
                    optionsData={eventTriggerOptions}
                    register={register}
                    selectName="trigger"
                    error={errors.trigger}
                    placeHolder="select function trigger"
                    setValue={setValue}
                  />
                  <InputTitle>Bind function to shelf</InputTitle>
                  <InputSubTitle>
                    Select on what shelf you want function to be executed
                  </InputSubTitle>
                  <Select
                    optionsData={data}
                    register={register}
                    selectName="shelfId"
                    error={errors.shelfId}
                    placeHolder="select shelf"
                    setValue={setValue}
                  />
                </>
              ) : (
                <>
                  <InputTitle>Choose predifined function</InputTitle>
                  <InputSubTitle>
                    Choose function that suits your needs
                  </InputSubTitle>
                  <Select
                    optionsData={functionOptions}
                    register={register}
                    selectName="function"
                    error={errors.function}
                    placeHolder="select function"
                    setValue={setValue}
                  />
                  {predefinedFunction && (
                    <>
                      <InputTitle>Function Name</InputTitle>
                      <InputSubTitle>
                        Enter a name that describes the purpose of your function
                      </InputSubTitle>
                      <InputField
                        placeholder="Function name"
                        error={errors.name}
                        {...register('name', validations)}
                      />
                      <InputTitle>Bind function to shelf</InputTitle>
                      <InputSubTitle>
                        Select on what shelf you want function to be executed
                      </InputSubTitle>
                      <Select
                        optionsData={data}
                        register={register}
                        selectName="shelfId"
                        error={errors.shelfId}
                        placeHolder="select shelf"
                        setValue={setValue}
                      />
                    </>
                  )}
                  {predefinedFunction === 'backup' && (
                    <>
                      <InputTitle>Select backup shelf</InputTitle>
                      <InputSubTitle>
                        Where you want your data to be stored
                      </InputSubTitle>
                      <Select
                        optionsData={data}
                        register={register}
                        validation={validateBackupShelf}
                        selectName="functionParam"
                        error={errors.functionParam}
                        placeHolder="select backup shelf"
                      />
                    </>
                  )}
                  {predefinedFunction === 'log' && (
                    <>
                      <InputTitle>Select event trigger</InputTitle>
                      <InputSubTitle>
                        When should function be executed
                      </InputSubTitle>
                      <Select
                        optionsData={logTrigger}
                        register={register}
                        selectName="trigger"
                        error={errors.trigger}
                        placeHolder="select function trigger"
                      />
                    </>
                  )}
                </>
              )}
            </FunctionFormInner>
            <ModalButtonDivider>
              <Button
                variant="lightBordered"
                onClick={handleCloseModal}
                size="large"
              >
                Cancel
              </Button>
              <Button size="large">Submit</Button>
            </ModalButtonDivider>
          </FunctionForm>
        </FunctionContainer>
      </FunctionModalContainer>
    </Backdrop>
  );

  const modalPortal = document.getElementById('modal-root');
  return <>{modalPortal ? ReactDOM.createPortal(modal, modalPortal) : null}</>;
};

export default FunctionModal;

import { SyntheticEvent, useState } from 'react';
import ReactDOM from 'react-dom';
import { useForm } from 'react-hook-form';
import { FaRegTimesCircle } from 'react-icons/fa';
import { toast } from 'react-toastify';
import { FunctionForm, FunctionFormInner } from '../form/form-styles';
import { InputField } from '../UI/input/InputField';
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
  FunctionModalFooter,
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
import { config } from '../../utils/validation/config/functionModalValidations';

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

  const onSubmit = (formData: FunctionFormData) => {
    const newFormData: FunctionFormData = formData;
    newFormData.name = newFormData.name.trim();
    if (formData.function === 'backup') {
      const newData = { ...formData };
      newData.eventId = 1;
      functionService
        .createPredefinedFunction(newData)
        .then((res) => {
          toast.success(res.data.message);
          onCloseModal();
          onGetData();
        })
        .catch((err) => toast.error(err.response?.data.message));
    } else if (formData.function === 'log') {
      functionService
        .createPredefinedFunction(formData)
        .then((res) => {
          toast.success(res.data.message);
          onCloseModal();
          onGetData();
        })
        .catch((err) => toast.error(err.response?.data.message));
    } else {
      functionService
        .createCustomfunction(formData)
        .then((res) => {
          toast.success(res.data.message);
          onCloseModal();
          onGetData();
        })
        .catch((err) => {
          toast.error(err.response?.data.message);
        });
    }
  };

  const predefinedFunction = watch('function');
  const validations = config(watch);

  const handleChange = ({ target }: React.ChangeEvent<HTMLInputElement>) => {
    setOptionValue(target.value);
    reset();
  };

  const handleEventPropagation = (event: SyntheticEvent) => {
    event.stopPropagation();
  };

  const modal = (
    <Backdrop onClick={onCloseModal}>
      <FunctionModalContainer onClick={handleEventPropagation}>
        <FunctionHeader>
          <HeaderItem>
            <H2>Create Function</H2>
          </HeaderItem>

          <HeaderItem>
            <Close onClick={onCloseModal}>
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
                    Build Shelf Function with prewritten code and configuration
                    preset for common use cases.
                  </RadioSubTitle>
                </div>
              </RadioInner>
              <input
                type="radio"
                id="blueprint"
                name="code"
                value="blueprint"
                checked={optionValue === 'blueprint'}
                onChange={handleChange}
              />
              <img
                src={`${process.env.PUBLIC_URL}/assets/images/coding.png`}
                alt="code symbol"
              />
            </RadioLabel>
          </RadioContainer>
          <FunctionForm onSubmit={handleSubmit(onSubmit)} id="function-form">
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
                    {...register('name', validations.functionValidation)}
                  />
                  <Error>{errors?.functionName?.message}</Error>
                  <InputTitle>Runtime</InputTitle>
                  <InputSubTitle>
                    Choose the language to use for writing your function
                  </InputSubTitle>
                  <Select
                    optionsData={languageOptions}
                    register={register}
                    selectName="language"
                    error={errors.language}
                    placeHolder="Select language"
                    setValue={setValue}
                    validation={validations.basicValidation}
                  />
                  <InputTitle>Select event trigger</InputTitle>
                  <InputSubTitle>
                    When should function be executed
                  </InputSubTitle>
                  <Select
                    optionsData={eventTriggerOptions}
                    register={register}
                    selectName="eventId"
                    error={errors.eventId}
                    placeHolder="Select function trigger"
                    setValue={setValue}
                    validation={validations.basicValidation}
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
                    placeHolder="Select shelf"
                    setValue={setValue}
                    validation={validations.basicValidation}
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
                    placeHolder="Select function"
                    setValue={setValue}
                    validation={validations.basicValidation}
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
                        {...register('name', validations.functionValidation)}
                      />
                      <InputTitle>Bind function to shelf</InputTitle>
                      <InputSubTitle>
                        Select on what shelf you want function to be executed on
                      </InputSubTitle>
                      <Select
                        optionsData={data}
                        register={register}
                        selectName="shelfId"
                        error={errors.shelfId}
                        placeHolder="Select shelf"
                        setValue={setValue}
                        validation={validations.basicValidation}
                      />
                    </>
                  )}
                  {predefinedFunction === 'backup' && (
                    <>
                      <InputTitle>Select backup shelf</InputTitle>
                      <InputSubTitle>
                        Data will be backed up upon uploading files in bound
                        shelf
                      </InputSubTitle>
                      <Select
                        optionsData={data}
                        register={register}
                        selectName="functionParam"
                        error={errors.functionParam}
                        placeHolder="Select backup shelf"
                        setValue={setValue}
                        validation={validations.backupShelf}
                      />
                    </>
                  )}
                  {predefinedFunction === 'log' && (
                    <>
                      <InputTitle>Log file name</InputTitle>
                      <InputSubTitle>
                        File will be created on first level of selected shelf
                      </InputSubTitle>
                      <InputField
                        placeholder="Log file name"
                        error={errors.name}
                        {...register(
                          'logFileName',
                          validations.functionValidation
                        )}
                      />
                      <InputTitle>Select event trigger</InputTitle>
                      <InputSubTitle>
                        When should function be executed
                      </InputSubTitle>
                      <Select
                        optionsData={logTrigger}
                        register={register}
                        selectName="eventId"
                        error={errors.eventId}
                        setValue={setValue}
                        placeHolder="Select function trigger"
                        validation={validations.basicValidation}
                      />
                    </>
                  )}
                </>
              )}
            </FunctionFormInner>
          </FunctionForm>
        </FunctionContainer>
        <FunctionModalFooter>
          <Button
            variant="lightBordered"
            onClick={onCloseModal}
            size="large"
            type="button"
          >
            Cancel
          </Button>
          <Button size="large" type="submit" form="function-form">
            Submit
          </Button>
        </FunctionModalFooter>
      </FunctionModalContainer>
    </Backdrop>
  );

  const modalPortal = document.getElementById('modal-root');
  return <>{modalPortal ? ReactDOM.createPortal(modal, modalPortal) : null}</>;
};

export default FunctionModal;

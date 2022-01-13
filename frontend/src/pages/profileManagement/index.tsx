import { useEffect, useState } from 'react';
import { useForm } from 'react-hook-form';
import { AiFillEdit } from 'react-icons/ai';
import { toast } from 'react-toastify';
import { Header } from '../../components';
import { Base, InputFieldWrapper } from '../../components/form/form-styles';
import { ProfilePicture } from '../../components/header/header-styles';
import { Container, Wrapper } from '../../components/layout/layout.styles';
import Modal from '../../components/modal';
import UploadPictureModal from '../../components/modal/uploadPictureModal';
import {
  ProfileLeft,
  ProfileWrapper,
  EditImageContainer,
  ProfileImageContainer,
  ProfileRight,
  ButtonWrapper,
} from '../../components/profile/profile.styles';
import { H2 } from '../../components/text/text-styles';
import { Button } from '../../components/UI/button';
import { InputField } from '../../components/UI/input/InputField';
import {
  ManageProfileFieldConfig,
  ManageProfileFormData,
} from '../../interfaces/dataTypes';
import fileServices from '../../services/fileServices';
import userServices from '../../services/userServices';
import { useAppDispatch, useAppSelector } from '../../store/hooks';
import { setUser } from '../../store/userReducer';
import { theme } from '../../theme';
import { config } from '../../utils/validation/config/manageProfileValidationConfig';
import PasswordRequirementsTooltip from '../register/passwordRequirementsTooltip';

const Profile = () => {
  const user = useAppSelector((state) => state.user.user);
  const passwordDefault = 'passwordD@123';
  const {
    register,
    watch,
    handleSubmit,
    setValue,
    formState: { errors },
  } = useForm<ManageProfileFormData>();

  const manageProfileFieldConfig: ManageProfileFieldConfig[] = config(watch);
  const [isDisabled, setIsDisabled] = useState(true);
  const [isOpen, setIsOpen] = useState(false);
  const [isPasswordTooltipVisible, setIsPasswordTooltipVisible] =
    useState(false);
  const [imgUrl, setImgUrl] = useState<string>(
    fileServices.getProfilePicture(user?.id)
  );
  const dispatch = useAppDispatch();

  const resetPassword = () => {
    setValue('password', passwordDefault, { shouldValidate: true });
    setValue('confirmPassword', passwordDefault, { shouldValidate: true });
  };

  useEffect(() => {
    if (user) {
      setValue('firstName', user.firstName, { shouldValidate: true });
      setValue('lastName', user.lastName, { shouldValidate: true });
      setValue('email', user.email, { shouldValidate: true });
      resetPassword();
      setImgUrl(fileServices.getProfilePicture(user.id));
    }
  }, [user]);

  const handleInputFocus = (e: React.FocusEvent<HTMLInputElement>) => {
    if (e.target.name === 'password') {
      setIsPasswordTooltipVisible(true);
    }
  };

  const handleInputBlur = (e: React.FocusEvent<HTMLInputElement>) => {
    if (e.target.name === 'password') {
      setIsPasswordTooltipVisible(false);
    }
  };

  const handleToggleIsDisabled = () => {
    setIsDisabled(!isDisabled);
    setValue('password', '', { shouldValidate: true });
    setValue('confirmPassword', '', { shouldValidate: true });
  };
  const handleCancelEditProfile = () => {
    resetPassword();
    setIsDisabled(!isDisabled);
  };
  const handleOpenModal = () => {
    setIsOpen(true);
  };
  const handleCloseModal = () => {
    setIsOpen(false);
  };

  const onSubmit = (data: ManageProfileFormData) => {
    if (user) {
      let newPassword: string | null = data.password.trim();
      let newFirstName: string | null = data.firstName.trim();
      let newLastName: string | null = data.lastName.trim();

      if (
        (newFirstName === user.firstName &&
          newLastName === user.lastName &&
          newPassword === '') ||
        (newFirstName === '' && newLastName === '' && newPassword === '')
      ) {
        handleToggleIsDisabled();
        resetPassword();
        return;
      }

      if (newPassword === '') {
        newPassword = null;
      }
      if (newFirstName === user?.firstName) {
        newFirstName = null;
      }
      if (newLastName === user?.lastName) {
        newLastName = null;
      }

      userServices
        .updateProfile(
          {
            password: newPassword,
            firstName: newFirstName,
            lastName: newLastName,
          },
          user.id
        )
        .then((res) => {
          setValue('firstName', res.data.firstName, { shouldValidate: true });
          setValue('lastName', res.data.lastName, { shouldValidate: true });
          setValue('email', res.data.email, { shouldValidate: true });

          dispatch(
            setUser({
              ...user,
              firstName: data.firstName,
              lastName: data.lastName,
            })
          );

          toast.success('User successfully updated');
        })
        .catch((err) => {
          toast.success(err.response?.data?.message);
        })
        .finally(() => {
          handleToggleIsDisabled();
          resetPassword();
        });
    }
  };

  const handleUpdateImage = (url: string) => {
    setImgUrl(url);
  };

  return (
    <>
      {isOpen && user && (
        <Modal onCloseModal={handleCloseModal}>
          <UploadPictureModal
            onCloseModal={handleCloseModal}
            id={user.id}
            onUpdatePicture={handleUpdateImage}
          />
        </Modal>
      )}
      <Header position="absolute" />
      <Wrapper>
        <Container>
          <ProfileWrapper>
            <ProfileLeft>
              <ProfileImageContainer>
                {imgUrl && (
                  <ProfilePicture imgUrl={imgUrl} size={theme.space.xxl} />
                )}
                <EditImageContainer>
                  <AiFillEdit size={theme.space.lg} onClick={handleOpenModal} />
                </EditImageContainer>
              </ProfileImageContainer>
            </ProfileLeft>
            <ProfileRight>
              <Base onSubmit={handleSubmit(onSubmit)}>
                <H2>My Profile</H2>
                <InputFieldWrapper>
                  {manageProfileFieldConfig.map(
                    (fieldConfig: ManageProfileFieldConfig) => (
                      <InputField
                        key={fieldConfig.name}
                        disabled={
                          fieldConfig.name === 'email' ? true : isDisabled
                        }
                        error={errors[fieldConfig.name]}
                        type={fieldConfig.type}
                        {...register(fieldConfig.name, fieldConfig.validations)}
                        onFocus={handleInputFocus}
                        onBlur={handleInputBlur}
                      />
                    )
                  )}
                  {isPasswordTooltipVisible && <PasswordRequirementsTooltip />}
                </InputFieldWrapper>

                <ButtonWrapper>
                  <Button
                    type="button"
                    onClick={handleToggleIsDisabled}
                    disabled={!isDisabled}
                  >
                    Edit
                  </Button>
                  {!isDisabled && <Button type="submit">Update</Button>}
                  <Button
                    disabled={isDisabled}
                    type="button"
                    variant={isDisabled ? 'secondary' : 'secondary'}
                    onClick={handleCancelEditProfile}
                  >
                    Cancel
                  </Button>
                </ButtonWrapper>
              </Base>
            </ProfileRight>
          </ProfileWrapper>
        </Container>
      </Wrapper>
    </>
  );
};

export default Profile;

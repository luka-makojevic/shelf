/* eslint-disable no-plusplus */
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
  const dispatch = useAppDispatch();
  const imgUrl = fileServices.getProfilePicture(user?.id);

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
    }
  }, [user]);

  const handleToggleDisability = () => {
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
    let newPassword: string | null = data.password.trim();
    let newFirstName: string | null = data.firstName.trim();
    let newLastName: string | null = data.lastName.trim();

    if (
      (user &&
        newFirstName === user.firstName &&
        newLastName === user.lastName &&
        newPassword === '') ||
      (newFirstName === '' && newLastName === '' && newPassword === '')
    ) {
      handleToggleDisability();
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
        20
      )
      .then((res) => {
        if (user) {
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
        }

        toast.success('User successfully updated');
      })
      .catch((err) => {
        toast.success(err.response?.data?.message);
      })
      .finally(() => {
        handleToggleDisability();
        resetPassword();
      });
  };

  return (
    <>
      {isOpen && (
        <Modal onCloseModal={handleCloseModal}>
          <UploadPictureModal onCloseModal={handleCloseModal} />
        </Modal>
      )}
      <Header position="absolute" />
      <Wrapper>
        <Container>
          <ProfileWrapper>
            <ProfileLeft>
              <ProfileImageContainer>
                <ProfilePicture imgUrl={imgUrl} size={theme.space.xxl} />
                <EditImageContainer>
                  <AiFillEdit size={theme.space.lg} onClick={handleOpenModal} />
                </EditImageContainer>
              </ProfileImageContainer>
              {/* <About>Member since March</About> TODO - get createdAt from BE */}
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
                      />
                    )
                  )}
                </InputFieldWrapper>

                <ButtonWrapper>
                  <Button
                    type="button"
                    onClick={handleToggleDisability}
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

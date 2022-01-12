/* eslint-disable no-plusplus */
import { useState } from 'react';
import { useForm } from 'react-hook-form';
import { AiFillEdit } from 'react-icons/ai';
import { Header } from '../../components';
import { Title } from '../../components/alert/alert-styles';
import { Base, InputFieldWrapper } from '../../components/form/form-styles';
import { Container, Wrapper } from '../../components/layout/layout.styles';
import Modal from '../../components/modal';
import UploadPictureModal from '../../components/modal/uploadPictureModal';
import {
  ProfileLeft,
  ProfileImage,
  ProfileWrapper,
  EditImageContainer,
  ProfileImageContainer,
  About,
  ProfileRight,
  ButtonWrapper,
} from '../../components/profile/profile.styles';
import { Button } from '../../components/UI/button';
import { InputField } from '../../components/UI/input/InputField';
import {
  ManageProfileFieldConfig,
  ManageProfileFormData,
} from '../../interfaces/dataTypes';
import userServices from '../../services/userServices';
// import { useAppSelector } from '../store/hooks';
import { theme } from '../../theme';
import { config } from '../../utils/validation/config/manageProfileValidationConfig';

const user = {
  id: 1,
  firstName: 'Sanja',
  lastName: 'Nikolic',
  email: 'sanja@nikolic.com',
  createdAt: new Date(),
  password: 'sanjaNikolic@1',
};

const Profile = () => {
  //   const user = useAppSelector((state) => state.user.user); - get user from global storage
  const {
    register,
    watch,
    handleSubmit,
    reset,
    formState: { errors },
  } = useForm<ManageProfileFormData>({
    defaultValues: {
      firstName: user.firstName,
      lastName: user.lastName,
      email: user.email,
      password: user.password,
      confirmPassword: user.password,
    },
  });
  const manageProfileFieldConfig: ManageProfileFieldConfig[] = config(watch);

  const [isDisabled, setIsDisabled] = useState(true);
  const [isOpen, setIsOpen] = useState(false);

  const onSubmit = ({
    firstName,
    lastName,
    email,
    password,
  }: ManageProfileFormData) => {
    if (
      (user &&
        firstName === user.firstName &&
        lastName === user.lastName &&
        email === user.email &&
        password === user.password) ||
      (firstName.trim().split('').join('') === '' &&
        lastName.trim().split('').join('') === '' &&
        email.trim().split('').join('') === '' &&
        password.trim().split('').join('') === '')
    ) {
      reset();
      console.log('staje');
      return;
    }
    console.log('ide dalje');

    userServices
      .updateProfile({ lastName, firstName, email, password })
      .then((res) => {
        console.log(res);
      })
      .catch((err) => console.log(err))
      .finally(() => reset());
  };

  const handleToggleDisability = () => {
    if (!isDisabled && Object.keys(errors).length !== 0) {
      return;
    }

    setIsDisabled(!isDisabled);
  };
  const handleCancelEditProfile = () => {
    reset();
    setIsDisabled(!isDisabled);
  };

  const handleUpdateProfilePicture = () => {
    // TODO - add update profile picture logic
    setIsOpen(true);
  };

  const handleCloseModal = () => {
    setIsOpen(false);
  };

  return (
    <>
      {isOpen && (
        <Modal onCloseModal={handleCloseModal}>
          <UploadPictureModal onCloseModal={handleCloseModal} />
        </Modal>
      )}
      <Header hideProfile position="absolute" />
      <Wrapper>
        <Container>
          <ProfileWrapper>
            <ProfileLeft>
              <ProfileImageContainer>
                <ProfileImage
                  src="./assets/images/profile.jpg"
                  alt="profile.jpg"
                />
                <EditImageContainer>
                  <AiFillEdit
                    size={theme.space.lg}
                    onClick={handleUpdateProfilePicture}
                  />
                </EditImageContainer>
              </ProfileImageContainer>
              <About>Member since {user?.createdAt.toLocaleDateString()}</About>
            </ProfileLeft>
            <ProfileRight>
              <Base onSubmit={handleSubmit(onSubmit)}>
                <Title>My Profile</Title>
                <InputFieldWrapper>
                  {manageProfileFieldConfig.map(
                    (fieldConfig: ManageProfileFieldConfig) => (
                      <InputField
                        key={fieldConfig.name}
                        disabled={isDisabled}
                        error={errors[fieldConfig.name]}
                        type={fieldConfig.type}
                        {...register(fieldConfig.name, fieldConfig.validations)}
                      />
                    )
                  )}
                </InputFieldWrapper>

                <ButtonWrapper>
                  <Button
                    type={!isDisabled ? 'button' : 'submit'}
                    onClick={handleToggleDisability}
                  >
                    {isDisabled ? 'Edit' : 'Update'}
                  </Button>
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

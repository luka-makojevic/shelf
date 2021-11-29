import React from 'react';
import { render, screen, fireEvent, waitFor } from '@testing-library/react';
import { ThemeProvider } from 'styled-components';
import { theme } from '../theme';
import FormValidation from '../pages/login/formValidation';

const mockLogin = jest.fn((email, password) =>
  Promise.resolve({ email, password })
);

describe('Login', () => {
  beforeEach(() => {
    render(
      <ThemeProvider theme={theme}>
        <FormValidation login={mockLogin} />
      </ThemeProvider>
    );
  });
  it('should display required error when value is invalid', async () => {
    fireEvent.submit(screen.getByRole('button'));

    expect(await screen.findAllByRole('alert')).toHaveLength(2);
    expect(mockLogin).not.toBeCalled();
  });

  it('should display matching error when email is invalid', async () => {
    fireEvent.input(screen.getByPlaceholderText('Email'), {
      target: {
        value: 'test',
      },
    });

    fireEvent.input(screen.getByPlaceholderText('Password'), {
      target: {
        value: 'Nemanja@123',
      },
    });

    fireEvent.submit(screen.getByRole('button'));

    expect(await screen.findAllByRole('alert')).toHaveLength(1);
    expect(mockLogin).not.toBeCalled();
    expect(screen.getByPlaceholderText('Email').value).toBe('test');
    expect(screen.getByPlaceholderText('Password').value).toBe('Nemanja@123');
  });

  it('should display min length error when password is invalid', async () => {
    fireEvent.input(screen.getByPlaceholderText('Email'), {
      target: {
        value: 'test@mail.com',
      },
    });

    fireEvent.input(screen.getByPlaceholderText('Password'), {
      target: {
        value: 'pass',
      },
    });

    fireEvent.submit(screen.getByRole('button'));

    expect(await screen.findAllByRole('alert')).toHaveLength(1);
    expect(mockLogin).not.toBeCalled();
    expect(screen.getByPlaceholderText('Email').value).toBe('test@mail.com');
    expect(screen.getByPlaceholderText('Password').value).toBe('pass');
  });

  it('should not display error when value is valid', async () => {
    fireEvent.input(screen.getByPlaceholderText('Email'), {
      target: {
        value: 'test@mail.com',
      },
    });

    fireEvent.input(screen.getByPlaceholderText('Password'), {
      target: {
        value: 'Nemanja@123',
      },
    });

    fireEvent.submit(screen.getByRole('button'));

    await waitFor(() => expect(screen.queryAllByRole('alert')).toHaveLength(0));
    expect(mockLogin).toBeCalledWith('test@mail.com', 'Nemanja@123');
    expect(screen.getByPlaceholderText('Email').value).toBe('');
    expect(screen.getByPlaceholderText('Password').value).toBe('');
  });
});

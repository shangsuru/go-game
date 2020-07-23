import React, {useState} from 'react';
import {Button, Card, Input} from 'antd';
import {CheckOutlined} from '@ant-design/icons';
import history from '../../history';
import api from '../../api';

const PasswordResetRequest = () => {
  const [email, setEmail] = useState('');
  const [error, setError] = useState('');

  const createErrorMessage = (error) => {
    setError(error);
    setTimeout(() => setError(''), 3000);
  };

  const handleReset = () => {
    if (email.length < 1) {
      createErrorMessage('No input given.');
      return;
    }
    api
      .post('/users/resetpassword', {email})
      .then((res) => {
        if (res.status === 200) {
          createErrorMessage("An email has been sent to you with a link to reset your password.");
        } else if (res.status === 204) {
          createErrorMessage('No user with that email found.');
        } else {
          createErrorMessage(
            'Unknown error occurred. If this keeps happening please contact tudagrouphs@gmail.com.'
          );
        }
      })
      .catch((err) => {
        console.log(err);
      });
  };

  const returnToLogin = () => {
    history.push('/login');
  };

  const handleKeyPress = (event) => {
    if (event.key === 'Enter') {
      handleReset();
    }
  };

  return (
    <div className='entry'>
      <Card>
        <h1 className='entry__title'>Password reset</h1>
        <p className='entry__error'>{error}</p>
        <div className='entry__input'>
          Email
          <Input
            value={email}
            onChange={(e) => setEmail(e.target.value)}
            onKeyPress={(e) => handleKeyPress(e)}
          />
        </div>
        <Button
          type='primary'
          className='entry__button'
          onClick={() => handleReset()}
        >
          <CheckOutlined/> Email me a link
        </Button>
      </Card>
    </div>
  );
};

export default PasswordResetRequest;

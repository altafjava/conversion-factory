import React from 'react';

const AlertMessage = ({ background, message }) => {
  return (
    <div style={{ background: background, color: 'white', width: '500px', textAlign: 'center', borderRadius: '4px' }}>
      <p>{message}</p>
    </div>
  );
};

export default AlertMessage;

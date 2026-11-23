import React, { useState } from 'react';
import './index.css';

import Office from './components/Office';
import OfficeDisplay from './components/OfficeDisplay';
import Button from './components/Button';

function App() {
  const [page, setPage] = useState('home');

  if (page === 'save') {
    return (
      <div className="container">
        <Office mode="save" />

        <Button
          buttonLabel="Back to Home"
          onClickButton={() => setPage('home')}
        />
      </div>
    );
  }

  if (page === 'delete') {
    return (
      <div className="container">
        <Office mode="delete" />

        <Button
          buttonLabel="Back to Home"
          onClickButton={() => setPage('home')}
        />
      </div>
    );
  }

  if (page === 'display') {
    return (
      <div className="container">
        <OfficeDisplay />

        <Button
          buttonLabel="Back to Home"
          onClickButton={() => setPage('home')}
        />
      </div>
    );
  }

  return (
    <div className="home">
      <h1>Clinic Management System</h1>
      <h2>Home</h2>

      <div className="buttons">
        <Button
          buttonLabel="Save Office"
          onClickButton={() => setPage('save')}
        />

        <Button
          buttonLabel="Delete Office"
          onClickButton={() => setPage('delete')}
        />

        <Button
          buttonLabel="Display Offices"
          onClickButton={() => setPage('display')}
        />
      </div>
    </div>
  );
}

export default App;
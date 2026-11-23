import React from 'react';

function Button({ buttonLabel, onClickButton }) {
  return (
    <span>
      <button onClick={onClickButton}>
        {buttonLabel}
      </button>
    </span>
  );
}

export default Button;
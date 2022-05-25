import {useState} from "react";

export const BasicLoginForm = ({onSignIn}) => {
  const [emailInput, setEmailInput] = useState('');
  const [passwordInput, setPasswordInput] = useState('');

  const submit = () => {
    onSignIn(emailInput, passwordInput)
    setEmailInput('');
    setPasswordInput('');
  }

  return (
    <div>
      <div style={{display: "flex", flexDirection: "column", alignItems: "start"}}>
        <label>
          Email:
          <input type={"text"} onChange={(e) => setEmailInput(e.target.value)} />
        </label>
        <label>
          Password:
          <input type={"password"} onChange={(e) => setPasswordInput(e.target.value)} />
        </label>
        <button onClick={() => submit()}>Sign in</button>
      </div>
    </div>
  )
}
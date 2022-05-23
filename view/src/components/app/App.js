import logo from '../../logo.svg';
import './App.css';
import {doPost} from "../../utils/fetch-utils";
import {useState} from "react";
import { ContentRouting } from "../../router/ContentRouting";

const SignInView = ({signIn}) => {

  const [emailInput, setEmailInput] = useState('');
  const [passwordInput, setPasswordInput] = useState('');

  const onSignIn = () => {
    signIn(emailInput, passwordInput)
    setEmailInput('');
    setPasswordInput('');
  }

  return (
    <div style={{display: "flex", flexDirection: "column", alignItems: "start"}}>
      <label>
        Email:
        <input type={"text"} onChange={(e) => setEmailInput(e.target.value)} />
      </label>
      <label>
        Password:
        <input type={"password"} onChange={(e) => setPasswordInput(e.target.value)} />
      </label>
      <button onClick={() => onSignIn()}>Sign in</button>
    </div>
  )
}

function App() {
  const [user, setUser] = useState({authenticated: false})

  const signIn = (email, password) => {
    const body = {
      email: email,
      password: password,
    }

    doPost("/api/auth/login", body, false)
      .then(response => {
        if (!response.ok) {
          throw new Error("Invalid credentials")
        }

        const jwt = response.headers.get("Authorization")
        localStorage.setItem("ACCESS_TOKEN", jwt)
        return response.json()
      })
      .then(userInfo => {
        setUser({
          ...user,
          authenticated: true,
          ...userInfo
        });
        //history.push("/dashboard");
        //history.go(0);
      })
      .catch(error => console.error(error))
  }

  const logout = () => {
    setUser({authenticated: false})
  }

  if (user.authenticated) {
    return (
      <div>
        {/*<h4>{`Hi, ${user.name}`}</h4>*/}
        {/*<button onClick={logout}>Log out</button>*/}
        <ContentRouting/>
      </div>
    )
  }

  return (
    <div className="App">
      <header className="App-header">
        <img src={logo} className="App-logo" alt="logo"/>
        <SignInView signIn={signIn} />
      </header>
    </div>
  );
}

export default App;

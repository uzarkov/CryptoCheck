import './App.css';
import {useState} from "react";
import {ProtectedContentRouting} from "../../router/ProtectedContentRouting";
import {PublicContentRouting} from "../../router/PublicContentRouting";

function App() {
  const [user, setUser] = useState({currentUser: null, authenticated: false})

  const logout = () => {
    setUser({authenticated: false})
    localStorage.removeItem("ACCESS_TOKEN")
  }

  if (user.authenticated) {
    return <ProtectedContentRouting user={user} logout={logout}/>
  } else {
    return <PublicContentRouting user={user} setUser={setUser}/>
  }
}

export default App;

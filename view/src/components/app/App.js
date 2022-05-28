import './App.css';
import {useState} from "react";
import {ProtectedContentRouting} from "../../router/ProtectedContentRouting";
import {PublicContentRouting} from "../../router/PublicContentRouting";
import {Box, createTheme, CssBaseline, ThemeProvider} from "@mui/material";
import {indigo} from "@mui/material/colors";

const theme = createTheme({
  palette: {
    primary: {
      main: indigo[900]
    }
  }
})

function App() {
  const [user, setUser] = useState({currentUser: null, authenticated: false})

  const logout = () => {
    setUser({currentUser: null, authenticated: false})
    localStorage.removeItem("ACCESS_TOKEN")
  }

  return (
    <ThemeProvider theme={theme}>
      <Box sx={{display: "flex"}}>
        <CssBaseline/>
        {
          user.authenticated
            ? <ProtectedContentRouting user={user} logout={logout}/>
            : <PublicContentRouting user={user} setUser={setUser}/>
        }
      </Box>
    </ThemeProvider>
  )
}

export default App;

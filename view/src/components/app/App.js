import './App.css';
import {useEffect, useState} from "react";
import {ProtectedContentRouting} from "../../router/ProtectedContentRouting";
import {PublicContentRouting} from "../../router/PublicContentRouting";
import {Box, createTheme, CssBaseline, ThemeProvider} from "@mui/material";
import {indigo} from "@mui/material/colors";
import {useLocation} from "react-router-dom";

const theme = createTheme({
  palette: {
    primary: {
      main: indigo[500],
    }
  }
})

function App() {
  const [user, setUser] = useState({currentUser: null, authenticated: false})
  const [requestedLocation, setRequestedLocation] = useState()
  const location = useLocation()

  useEffect(() => {
    setRequestedLocation(location)
  }, [])

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
            ? <ProtectedContentRouting user={user} logout={logout} requestedLocation={requestedLocation}/>
            : <PublicContentRouting setUser={setUser}/>
        }
      </Box>
    </ThemeProvider>
  )
}

export default App;

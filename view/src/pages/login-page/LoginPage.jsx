import {doGet, doPost, URL_PREFIX} from "../../utils/fetch-utils";
import {LoginForm} from "../../components/login/LoginForm";
import {useEffect} from "react";
import Box from '@mui/material/Box';
import { Container } from '@mui/material';
import CssBaseline from "@material-ui/core/CssBaseline";
import { MuiThemeProvider, createMuiTheme } from "@material-ui/core/styles";
import { ThemeProvider, createTheme } from '@mui/material/styles';
import Button from '@mui/material/Button';
import GitHubIcon from '@mui/icons-material/GitHub';

const darkTheme = createMuiTheme({
  palette: {
    background: {
        default: "#192752"
    }
  }
});

export const LoginPage = ({user, setUser}) => {
  useEffect(() => {
    if (localStorage.getItem("ACCESS_TOKEN") !== null) {
      doGet("/api/users/me")
        .then(response => response.json())
        .then(userInfo => {
          setUser({
            ...user,
            authenticated: true,
            currentUser: userInfo
          })
        })
        .catch(error => console.error(error))
    }
  }, [user, setUser])

  const signIn = (email, password) => {
    const body = {
      email: email,
      password: password,
    }

    doPost("/api/auth/login", body, false)
      .then(response => {
        const jwt = response.headers.get("Authorization")
        localStorage.setItem("ACCESS_TOKEN", jwt)
        return response.json()
      })
      .then(userInfo => {
        setUser({
          ...user,
          authenticated: true,
          currentUser: userInfo
        });
      })
      .catch(error => console.error(error))
  }

  return (
  <MuiThemeProvider theme={darkTheme}>
    <CssBaseline />
      <Container>
        <Box sx={{ display: 'flex', justifyContent: 'center'}}>
          <LoginForm onSignIn={(email, password) => signIn(email, password)}/>
        </Box>
      </Container>
  </MuiThemeProvider>
  )
}
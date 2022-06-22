import {doGet, doPost} from "../../utils/fetch-utils";
import {LoginForm} from "../../components/login/LoginForm";
import {useEffect, useState} from "react";
import Box from '@mui/material/Box';
import {Container} from '@mui/material';

export const LoginPage = ({setUser}) => {
  const [loggingIn, setLoggingIn] = useState(true)

  useEffect(() => {
    if (localStorage.getItem("ACCESS_TOKEN") !== null) {
      doGet("/api/users/me")
        .then(response => response.json())
        .then(userInfo => {
          setUser({
            authenticated: true,
            currentUser: userInfo
          })
        })
        .catch(error => console.error(error))
    } else {
      setLoggingIn(false)
    }

    return () => {
      setLoggingIn(false)
    }
  }, [setUser])

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
          authenticated: true,
          currentUser: userInfo
        });
      })
      .catch(error => console.error(error))
  }

  if (loggingIn) {
    return null;
  }

  return (
    <Container>
      <Box sx={{display: 'flex', justifyContent: 'center'}}>
        <LoginForm onSignIn={(email, password) => signIn(email, password)}/>
      </Box>
    </Container>
  )
}
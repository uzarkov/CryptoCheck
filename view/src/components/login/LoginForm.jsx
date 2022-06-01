import * as React from 'react';
import {useState} from "react";
import Avatar from '@mui/material/Avatar';
import Button from '@mui/material/Button';
import CssBaseline from '@mui/material/CssBaseline';
import TextField from '@mui/material/TextField';
import FormControlLabel from '@mui/material/FormControlLabel';
import Checkbox from '@mui/material/Checkbox';
import Link from '@mui/material/Link';
import Grid from '@mui/material/Grid';
import Box from '@mui/material/Box';
import GitHubIcon from '@mui/icons-material/GitHub';
import PersonIcon from '@mui/icons-material/Person';
import Typography from '@mui/material/Typography';
import Container from '@mui/material/Container';
import { createTheme, ThemeProvider } from '@mui/material/styles';
import {doGet, doPost, URL_PREFIX} from "../../utils/fetch-utils"
import { MuiThemeProvider, createMuiTheme } from "@material-ui/core/styles";
import '../app/App.css';
import { StyledEngineProvider } from '@mui/material/styles';


const theme = createMuiTheme();

export const LoginForm = ({onSignIn}) => {
  const [emailInput, setEmailInput] = useState('');
  const [passwordInput, setPasswordInput] = useState('');

  const submit = () => {
    onSignIn(emailInput, passwordInput)
    setEmailInput('');
    setPasswordInput('');
  }


function Copyright(props) {
  return (
    <Typography variant="body2" color="#9AA5C5" align="center" {...props}>
      {'Copyright © '}
      <Link color="inherit" href="https://mui.com/">
        CRYPTO
      </Link>{' '}
      {new Date().getFullYear()}
      {'.'}
    </Typography>
  );
}

  return (
    <StyledEngineProvider injectFirst>
        <MuiThemeProvider theme={theme}>
              <Container component="main" maxWidth="xs">
                <CssBaseline />
                <Box
                  sx={{
                    marginTop: 8,
                    display: 'flex',
                    flexDirection: 'column',
                    alignItems: 'center'
                  }}
                >
                  <Avatar
                   variant="circular"
                   sx={{ height:"100px", width:"100px", m: 1, color: 'black'}}>
                    <PersonIcon sx={{ fontSize:"60px"}} fontSize="large" />
                  </Avatar>
                  <Typography component="h1" variant="h5" sx={{ color:"#9AA5C5" }}>
                    Sign in
                  </Typography>
                  <Box component="form" noValidate sx={{ mt: 1}}>
                    <TextField
                      margin="normal"
                      variant="filled"
                      required
                      fullWidth
                      className="textBox-login"
                      type="text"
                      id="email"
                      label="Email Address"
                      name="email"
                      autoComplete="email"
                      onChange={(e) => setEmailInput(e.target.value)}
                    />
                    <TextField
                      margin="normal"
                      variant="filled"
                      required
                      fullWidth
                      className="textBox-login"
                      name="password"
                      label="Password"
                      type="password"
                      id="password"
                      autoComplete="current-password"
                      onChange={(e) => setPasswordInput(e.target.value)}
                    />
{/*                     <FormControlLabel */}
{/*                       control={<Checkbox value="remember" color="primary" />} */}
{/*                       label="Remember me" */}
{/*                     /> */}
                    <Button
                      onClick={() => submit()}
                      fullWidth
                      variant="contained"
                      sx={{ mt: 3, mb: 2 }}
                    >
                      Sign In
                    </Button>
                    <Button
                     fullWidth
                     variant="contained"
                     sx={{ backgroundColor: "black", mt: 3, mb: 2,
                           '&:hover': {
                                 backgroundColor: "black",}}}
                     startIcon={<GitHubIcon />}
                     href={`${URL_PREFIX}/oauth2/authorization/github`}>
                        Login with GitHub
                    </Button>
                    <Grid container>
                      <Grid item>
                        <Link href="#" variant="body2" sx={{ color:"#9AA5C5" }}>
                          {"Don't have an account? Sign Up"}
                        </Link>
                      </Grid>
                    </Grid>
                  </Box>
                </Box>
                <Copyright sx={{ mt: 8, mb: 4 }} />
              </Container>
            </MuiThemeProvider>
    </StyledEngineProvider>
  );
}
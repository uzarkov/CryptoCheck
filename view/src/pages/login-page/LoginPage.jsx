import {doGet, doPost, URL_PREFIX} from "../../utils/fetch-utils";
import {BasicLoginForm} from "../../components/login/BasicLoginForm";
import {useEffect} from "react";

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
    <div>
      <div>
        <a href={`${URL_PREFIX}/oauth2/authorization/github`}>Login with GitHub</a>
      </div>
      <BasicLoginForm onSignIn={(email, password) => signIn(email, password)}/>
    </div>
  )
}
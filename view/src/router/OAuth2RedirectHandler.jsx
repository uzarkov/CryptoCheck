import {Redirect, useLocation} from "react-router-dom";
import {doGet} from "../utils/fetch-utils";

export const OAuth2RedirectHandler = ({user, setUser}) => {
  const location = useLocation()

  const getUrlParameter = (parameterName) => {
    parameterName = parameterName.replace(/[\[]/, '\\[').replace(/[\]]/, '\\]');
    const regex = new RegExp('[\\?&]' + parameterName + '=([^&#]*)');
    const results = regex.exec(location.search);

    return results === null ? '' : decodeURIComponent(results[1].replace(/\+/g, ' '));
  }

  const fetchUserMetadata = () => {
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

  const token = getUrlParameter("access_token")
  if (token && !user.authenticated) {
    localStorage.setItem("ACCESS_TOKEN", token)
    fetchUserMetadata()
  }

  return (
    <Redirect to={{
      pathname: "/",
      state: {from: location}
    }}/>
  )
}
import {Redirect, Route, Switch} from "react-router-dom";
import {OAuth2RedirectHandler} from "./OAuth2RedirectHandler";
import {LoginPage} from "../pages/login-page/LoginPage";

export const PublicContentRouting = ({user, setUser}) => {

  return (
    <Switch>
      <Route path={"/"} exact>
        <LoginPage user={user} setUser={setUser} />
      </Route>

      <Route path={"/oauth2/response"}>
        <OAuth2RedirectHandler user={user} setUser={setUser} />
      </Route>

      <Route path="*">
        <Redirect to={"/"}/>
      </Route>
    </Switch>
  );
}
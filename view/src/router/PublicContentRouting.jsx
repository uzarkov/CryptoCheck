import {Redirect, Route, Switch} from "react-router-dom";
import {OAuth2RedirectHandler} from "./OAuth2RedirectHandler";
import {LoginPage} from "../pages/login-page/LoginPage";

export const PublicContentRouting = ({setUser}) => {

  return (
    <Switch>
      <Route path={"/"} exact>
        <LoginPage setUser={setUser} />
      </Route>

      <Route path={"/oauth2/response"}>
        <OAuth2RedirectHandler setUser={setUser} />
      </Route>

      <Route path="*">
        <Redirect to={"/"}/>
      </Route>
    </Switch>
  );
}
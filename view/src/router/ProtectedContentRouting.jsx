import { Redirect, Route, Switch } from "react-router-dom";

import { AnalyticsPage, URL as AnalyticsURL } from "../pages/analytics-page/AnalyticsPage";
import { DashboardPage, URL as DashboardURL } from "../pages/dashboard-page/DashboardPage";
import { PortfolioPage, URL as PortfolioURL } from "../pages/portfolio-page/PortfolioPage";
import { PositionsPage, URL as PositionsURL } from "../pages/positions-page/PositionsPage";
import { ErrorPage } from "../pages/error-page/ErrorPage";

export const ProtectedContentRouting = ({user, logout}) => {

    return (
      <div>
        <Switch>
          <Route path={"/"} exact>
            <Redirect to={DashboardURL}/>
          </Route>

          <Route path={DashboardURL} exact>
            <DashboardPage/>
          </Route>

          <Route path={AnalyticsURL} exact>
            <AnalyticsPage/>
          </Route>

          <Route path={PortfolioURL} exact>
            <PortfolioPage/>
          </Route>

          <Route path={PositionsURL} exact>
            <PositionsPage/>
          </Route>

          <Route path="*">
            <ErrorPage/>
          </Route>
        </Switch>

        <button onClick={() => logout()}>Log out</button>
      </div>
    );
}
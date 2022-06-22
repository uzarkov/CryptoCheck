import {Redirect, Route, Switch} from "react-router-dom";
import {AnalyticsPage, URL as AnalyticsURL} from "../pages/analytics-page/AnalyticsPage";
import {DashboardPage, URL as DashboardURL} from "../pages/dashboard-page/DashboardPage";
import {PortfolioPage, URL as PortfolioURL} from "../pages/portfolio-page/PortfolioPage";
import {PositionsPage, URL as PositionsURL} from "../pages/positions-page/PositionsPage";
import {ProfilePage, URL as ProfileURL} from "../pages/profile-page/ProfilePage";
import {ErrorPage} from "../pages/error-page/ErrorPage";
import {Sidebar} from "../components/sidebar/Sidebar";
import {useState} from "react";
import {Topbar} from "../components/topbar/Topbar";
import {Box, Container, Toolbar} from "@mui/material";

export const ProtectedContentRouting = ({user, logout}) => {
  const [isSidebarOpen, setIsSidebarOpen] = useState(false)

  return (
    <>
      <Topbar isOpen={isSidebarOpen} setIsOpen={setIsSidebarOpen} logout={logout}/>
      <Sidebar isOpen={isSidebarOpen} setIsOpen={setIsSidebarOpen}/>
      <Box
        component={"main"}
        sx={{
          flexGrow: 1,
          height: "100vh",
          overflow: "auto"
        }}
      >
        <Toolbar/>

        <Container maxWidth={"xlx"} sx={{mt: 4, mb: 4}}>
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

            <Route path={ProfileURL} exact>
              <ProfilePage user={user} />
            </Route>

            <Route path="*">
              <ErrorPage/>
            </Route>
          </Switch>
        </Container>

      </Box>
    </>
  );
}
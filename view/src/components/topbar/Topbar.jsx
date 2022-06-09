import {NAME as AnalyticsName, URL as AnalyticsURL} from "../../pages/analytics-page/AnalyticsPage";
import {NAME as DashboardName, URL as DashboardURL} from "../../pages/dashboard-page/DashboardPage";
import {NAME as PortfolioName, URL as PortfolioURL} from "../../pages/portfolio-page/PortfolioPage";
import {NAME as PositionsName, URL as PositionsURL} from "../../pages/positions-page/PositionsPage";
import {NAME as ProfileName, URL as ProfileURL} from "../../pages/profile-page/ProfilePage";
import IconButton from "@mui/material/IconButton"
import Toolbar from "@mui/material/Toolbar"
import Typography from "@mui/material/Typography"
import MenuIcon from "@mui/icons-material/Menu"
import {useHistory, useLocation} from "react-router-dom";
import {AccountCircle, Logout} from "@mui/icons-material";
import {MobileAppBar} from "./MobileAppBar";
import {DesktopAppBar} from "./DesktopAppBar";
import { StyledEngineProvider } from '@mui/material/styles';
import Avatar from "@mui/material/Avatar";

export const Topbar = ({isOpen, setIsOpen, logout, user}) => {
  const isMobile = window.matchMedia(`(max-width: 720px)`).matches;
  const history = useHistory()
  const location = useLocation()

  const currentViewName = () => {
    const pages = [
      {name: DashboardName, url: DashboardURL},
      {name: ProfileName, url: ProfileURL},
      {name: AnalyticsName, url: AnalyticsURL},
      {name: PortfolioName, url: PortfolioURL},
      {name: PositionsName, url: PositionsURL},
    ];

    const currentPage = pages.find(({url}) => location.pathname.startsWith(url))
    return currentPage?.name || "Dashboard"
  }

  const AppBar = isMobile ? MobileAppBar : DesktopAppBar;

  return (
   <StyledEngineProvider injectFirst>
    <AppBar isOpen={isOpen}>
         <Toolbar sx={{pr: "24px"}}>
           <IconButton
             className={"menu-topBar-item"}
             edge={"start"}
             color={"inherit"}
             aria-label={"open drawer"}
             onClick={() => setIsOpen(!isOpen)}
             sx={{marginRight: "36px", ...(!isMobile && isOpen && {display: "none"})}}
           >
             <MenuIcon/>
           </IconButton>
           <Typography
             className={"menu-topBar-item"}
             component={"h1"}
             variant={"h6"}
             color={"inherit"}
             noWrap
             sx={{flexGrow: 1}}
           >
             {currentViewName()}
           </Typography>
           <IconButton
             className={"menu-topBar-item"}
             edge={"end"}
             color={"inherit"}
             aria-label={"profile"}
             onClick={() => history.push(ProfileURL)}
             sx={{marginRight: "2px"}}
           >
             {
                user.currentUser.avatarUrl
                  ? <Avatar sx={{ width: 24, height: 24 }} src={user.currentUser.avatarUrl} />
                  : <AccountCircle/>
             }
           </IconButton>
           <IconButton
             className={"menu-topBar-item"}
             edge={"end"}
             color={"inherit"}
             aria-label={"logout"}
             onClick={() => logout()}
           >
             <Logout/>
           </IconButton>
         </Toolbar>
       </AppBar>
   </StyledEngineProvider>
  )
}
import {NAME as AnalyticsName, URL as AnalyticsURL} from "../../pages/analytics-page/AnalyticsPage";
import {NAME as DashboardName, URL as DashboardURL} from "../../pages/dashboard-page/DashboardPage";
import {NAME as PortfolioName, URL as PortfolioURL} from "../../pages/portfolio-page/PortfolioPage";
import {NAME as PositionsName, URL as PositionsURL} from "../../pages/positions-page/PositionsPage";
import {NAME as ProfileName, URL as ProfileURL} from "../../pages/profile-page/ProfilePage";
import ListItemButton from '@mui/material/ListItemButton';
import ListItemIcon from '@mui/material/ListItemIcon';
import ListItemText from '@mui/material/ListItemText';
import DashboardIcon from '@mui/icons-material/Dashboard';
import {Divider, List} from "@mui/material";
import {useHistory} from "react-router-dom";
import Toolbar from "@mui/material/Toolbar";
import IconButton from "@mui/material/IconButton";
import {AccountBalanceWallet, AccountBox, Analytics, AutoAwesomeMotion, ChevronLeft} from "@mui/icons-material";
import {DesktopDrawer} from "./DesktopDrawer";
import {MobileDrawer} from "./MobileDrawer";

export const Sidebar = ({isOpen, setIsOpen}) => {
  const isMobile = window.matchMedia(`(max-width: 720px)`).matches;
  const history = useHistory()

  const mainLinks = [
    [DashboardName, DashboardURL, <DashboardIcon/>],
    [ProfileName, ProfileURL, <AccountBox/>],
  ]

  const links = [
    [AnalyticsName, AnalyticsURL, <Analytics/>],
    [PortfolioName, PortfolioURL, <AccountBalanceWallet/>],
    [PositionsName, PositionsURL, <AutoAwesomeMotion/>],
  ];

  const createListItems = (items) => {
    return items.map(([name, url, icon], idx) => (
      <ListItemButton key={idx} onClick={() => {
        history.push(url)
        if (isMobile) {
          setIsOpen(false)
        }
      }}>
        <ListItemIcon>
          {icon}
        </ListItemIcon>
        <ListItemText primary={name}/>
      </ListItemButton>
    ))
  }

  const Drawer = isMobile ? MobileDrawer : DesktopDrawer

  return (
    <Drawer isOpen={isOpen} setIsOpen={setIsOpen}>
      <Toolbar
        sx={{
          display: "flex",
          alignItems: "center",
          justifyContent: "flex-end",
          px: [1],
        }}
      >
        <IconButton onClick={() => setIsOpen(!isOpen)}>
          <ChevronLeft />
        </IconButton>
      </Toolbar>
      <Divider />
      <List component={"nav"}>
        {createListItems(mainLinks)}
      </List>
      <Divider />
      <List component={"nav"}>
        {createListItems(links)}
      </List>
    </Drawer>
  )
}
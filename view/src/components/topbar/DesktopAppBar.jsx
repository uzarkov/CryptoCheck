import MuiAppBar from "@mui/material/AppBar"
import {styled} from "@mui/material";


const AppBar = styled(MuiAppBar, {
  shouldForwardProp: (prop) => prop !== "open",
})(({theme, open}) => ({
  zIndex: theme.zIndex.drawer + 1,
  transition: theme.transitions.create(['width', 'margin'], {
    easing: theme.transitions.easing.sharp,
    duration: theme.transitions.duration.leavingScreen,
  }),
  ...(open && {
    marginLeft: 240,
    width: `calc(100% - 240px)`,
    transition: theme.transitions.create(['width', 'margin'], {
      easing: theme.transitions.easing.sharp,
      duration: theme.transitions.duration.enteringScreen,
    }),
  }),
}));

export const DesktopAppBar = ({isOpen, children}) => {
  return (
    <AppBar position={"absolute"} open={isOpen}>
      {children}
    </AppBar>
  )
}
import Container from '@mui/material/Container';
import Box from '@mui/material/Box';
import CssBaseline from "@material-ui/core/CssBaseline";
import { MuiThemeProvider, createMuiTheme } from "@material-ui/core/styles";
import { ThemeProvider, createTheme } from '@mui/material/styles';


export const URL = '/portfolio';
export const NAME = "Portfolio";


export const PortfolioPage = () => {
    return (
        <Container>
            <Box sx={{ display: 'flex', justifyContent: 'center'}}>
                <div>
                    <h1>
                        Portfolio Page
                    </h1>
                </div>
            </Box>
        </Container>
    );
}
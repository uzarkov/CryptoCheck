import {Box, Card, CardHeader, Table, TableBody, TableCell, TableHead, TableRow} from "@mui/material";
import Typography from "@mui/material/Typography";
import '../../../global-styles/globalStyles.css';
import "./AbbreviatedPositionTable.css";
import {Link} from "react-router-dom";

export const AbbreviatedPositionsTable = ({positions}) => {

    return (
        <Card sx={{
            boxShadow: '0 0 3px #ccc',
            borderRadius: '15px'
        }}>
            <div className={"tableHeader"}>
                <CardHeader
                    title="Positions"
                    sx = {{
                        textAlign: 'center',
                    }}
                />
                <label className={"subtitle"}>
                    Recent records
                </label>
            </div>
            <Box sx={{ minWidth: 700 }}>
                    <Table>
                        <TableHead>
                            <TableRow>
                                <TableCell>
                                    Symbol
                                </TableCell>
                                <TableCell>
                                    Quantity
                                </TableCell>
                                <TableCell>
                                    Entry Price
                                </TableCell>
                                <TableCell>
                                    Current Price
                                </TableCell>
                                <TableCell>
                                    Profit
                                </TableCell>
                                <TableCell>
                                    Status
                                </TableCell>
                            </TableRow>
                        </TableHead>
                        <TableBody>
                            {positions.map((position) => (
                                <TableRow
                                    hover
                                    key={position.id}
                                >
                                    <TableCell>
                                        {position.cryptocurrency.symbol}
                                    </TableCell>
                                    <TableCell>
                                        {position.quantity}
                                    </TableCell>
                                    <TableCell>
                                        {`$${position.entryPrice}`}
                                    </TableCell>
                                    <TableCell>
                                        {`$${position.currentPrice}`}
                                    </TableCell>
                                    <TableCell
                                        sx={{
                                            color: position.percentageChange > 0
                                                ? '#388e3c'
                                                : (position.percentageChange < 0
                                                   ? '#d32f2f'
                                                   : '#fbc668'
                                                  )
                                        }}
                                    >
                                        {`${position.percentageChange}%`}
                                    </TableCell>
                                    <TableCell>
                                        <Typography
                                            sx={{
                                                mr: 1,
                                                color: position.closureDate === null ? '#388e3c' : '#fbc668',
                                                fontWeight: 'bold'
                                            }}

                                        >
                                            {position.closureDate === null ? 'OPEN' : 'CLOSED'}
                                        </Typography>
                                    </TableCell>
                                </TableRow>
                            ))}
                        </TableBody>
                    </Table>
                </Box>
            <div className={"tableFooter"}>
                <button
                    className={"linkButton"}
                    style={{paddingBottom: '15px', paddingTop: '5px'}}
                >
                    <Link
                        to={"/positions"}
                        className={"linkButton"}
                    >
                        View more
                    </Link>
                </button>
            </div>
        </Card>
    );
}
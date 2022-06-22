import { Avatar } from "@mui/material";
import Typography from "@mui/material/Typography";
import ArrowDownwardIcon from '@mui/icons-material/ArrowDownward';
import ArrowUpwardIcon from '@mui/icons-material/ArrowUpward';
import {useEffect, useState} from "react";
import "./CryptoPreference.css";
import "../../../../global-styles/globalStyles.css";

export const CryptoPreference = (props) => {

    const ERROR = 'error';
    const SUCCESS = 'success';

    const [isChangeNegative, setIsChangeNegative] = useState(false);

    useEffect(() => {
        setIsChangeNegative(props.change < 0);
    }, [])

    return (
        <div className={"shadowbox card"}>
            <div className={"crypto"}>
                <Typography
                    variant="h5"
                >
                    {props.symbol}
                </Typography>
                <Avatar
                    src={props.icon}
                    sx = {{
                        height: 50,
                        width: 50,
                    }}
                />
            </div>
            <Typography
                color="textPrimary"
                variant="h5"
            >
                {`$${props.price}`}
            </Typography>
            <div className={"change"}>
                {isChangeNegative
                    ? <ArrowDownwardIcon color={ERROR} width={10} height={10}/>
                    : <ArrowUpwardIcon color={SUCCESS}/>
                }
                <Typography
                    sx={{
                        mr: 1,
                        color: isChangeNegative ? '#d32f2f' : '#388e3c'
                    }}
                    variant="h6"
                >
                    {`${props.change}%`}
                </Typography>
                <Typography
                    color="textSecondary"
                    variant="caption"
                >
                    Last 24h
                </Typography>
            </div>
        </div>
    );
}
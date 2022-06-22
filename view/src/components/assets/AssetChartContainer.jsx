import {AssetChart} from "./AssetChart";
import {useEffect, useState} from "react";
import {doGet} from "../../utils/fetch-utils";
import Avatar from "@mui/material/Avatar";
import Radio from '@mui/material/Radio';
import RadioGroup from '@mui/material/RadioGroup';
import FormControlLabel from '@mui/material/FormControlLabel';
import FormControl from '@mui/material/FormControl';

const isMobile = window.matchMedia(`(max-width: 720px)`).matches;

export const AssetChartContainer = ({asset}) => {
  const [interval, setInterval] = useState("5m")
  const [data, setData] = useState([])

  useEffect(() => {
    doGet(`/api/prices/${asset.symbol}/${interval}`)
      .then(response => response.json())
      .then(candlestics => setData(candlestics))
  }, [interval, asset.symbol])

  return (
    <div style={{display: "flex", flexDirection: "column"}}>
      <div style={{display: "flex", flexDirection: "row", justifyContent: "space-between", marginBottom: "20px"}}>
        <Avatar src={asset.icon} sx={isMobile ? {marginRight: "16px"} : {}} />
        <FormControl>
          <RadioGroup
            defaultValue={"5m"}
            row
            value={interval}
            onChange={(e) => setInterval(e.target.value)}
          >
            <FormControlLabel value={"1m"} control={<Radio size={"small"} />} label={"1m"} />
            <FormControlLabel value={"5m"} control={<Radio size={"small"} />} label={"5m"} />
            <FormControlLabel value={"15m"} control={<Radio size={"small"} />} label={"15m"} />
            <FormControlLabel value={"1h"} control={<Radio size={"small"} />} label={"1h"} />
            <FormControlLabel value={"4h"} control={<Radio size={"small"} />} label={"4h"} />
            <FormControlLabel value={"8h"} control={<Radio size={"small"} />} label={"8h"} />
            <FormControlLabel value={"1d"} control={<Radio size={"small"} />} label={"1d"} />
          </RadioGroup>
        </FormControl>
      </div>
      <div>
        <canvas id={`${asset.symbol}-chart`} />
        <AssetChart asset={asset} data={data} />
      </div>
    </div>
  )
}
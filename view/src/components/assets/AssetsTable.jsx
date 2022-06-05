import Table from '@mui/material/Table';
import TableBody from '@mui/material/TableBody';
import TableCell from '@mui/material/TableCell';
import TableContainer from '@mui/material/TableContainer';
import TableHead from '@mui/material/TableHead';
import TableRow from '@mui/material/TableRow';
import Paper from '@mui/material/Paper';
import {useEffect, useState} from "react";
import {doGet} from "../../utils/fetch-utils";
import Avatar from "@mui/material/Avatar";
import {fixedDecimals, prettyPrint} from "../../utils/price-formatting";


const getCoinInfoUrl = (coingeckoCoinId) => `https://api.coingecko.com/api/v3/coins/${coingeckoCoinId}?tickers=false&market_data=true&community_data=false&developer_data=false&sparkline=false`

export const AssetTable = ({assetsMetadata, onRowClick}) => {
  const [assets, setAssets] = useState([])

  useEffect(() => {
    const promises = assetsMetadata.map(asset => {
      return doGet(getCoinInfoUrl(asset.coingeckoId))
        .then(response => response.json())
        .then(coingeckoData => ({
          name: asset.name,
          symbol: asset.symbol,
          icon: coingeckoData.image.small,
          price: asset.price,
          volume: coingeckoData["market_data"]["total_volume"]["usd"].toString(),
          mcap: coingeckoData["market_data"]["market_cap"]["usd"].toString()
        }))
        .catch(error => {
          console.error(error);
          return undefined;
        })
    })

    Promise.all(promises)
      .then(results => results.filter(res => res !== undefined))
      .then(results => setAssets(results))
      .catch(error => console.error(error))
  }, [assetsMetadata])

  return (
    <TableContainer component={Paper}>
      <Table>
        <TableHead>
          <TableRow>
            <TableCell>#</TableCell>
            <TableCell>Icon</TableCell>
            <TableCell>Name</TableCell>
            <TableCell>Symbol</TableCell>
            <TableCell>Price</TableCell>
            <TableCell>Volume (24h)</TableCell>
            <TableCell>Market cap</TableCell>
          </TableRow>
        </TableHead>
        <TableBody>
          {assets.map((asset, idx) => (
            <TableRow
              key={idx}
              onClick={() => onRowClick(asset)}
              hover={true}
              sx={{ '&:hover': {cursor: "pointer"}}}
            >
              <TableCell component={"th"} scope={"row"}>{idx + 1}</TableCell>
              <TableCell><Avatar src={asset.icon} sx={{width: 32, height: 32}} /></TableCell>
              <TableCell>{asset.name}</TableCell>
              <TableCell>{asset.symbol}</TableCell>
              <TableCell>{fixedDecimals(asset.price)} $</TableCell>
              <TableCell>{prettyPrint(asset.volume)} $</TableCell>
              <TableCell>{prettyPrint(asset.mcap)} $</TableCell>
            </TableRow>
          ))}
        </TableBody>
      </Table>
    </TableContainer>
  )
}
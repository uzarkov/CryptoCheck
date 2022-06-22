import * as React from 'react';
import { DataGrid, GridActionsCellItem } from '@mui/x-data-grid';
import DeleteIcon from '@mui/icons-material/Delete';
import MonetizationOnIcon from '@mui/icons-material/MonetizationOn';
import Button from '@mui/material/Button';
import Box from '@mui/material/Box';
import {useEffect, useState} from "react";
import {doGet, doDelete, doPatch, doPost} from "../../utils/fetch-utils";
import {DeleteDialog} from "./DeleteDialog";
import {CloseDialog} from "./CloseDialog";
import {AddDialog} from "./AddDialog";


// const getCoinInfoUrl = (coingeckoCoinId) => `https://api.coingecko.com/api/v3/coins/${coingeckoCoinId}?tickers=false&market_data=true&community_data=false&developer_data=false&sparkline=false`
// const positionsInfoUrl = (cryptocurrency) => `/api/cryptocurrency/${cryptocurrency}`

export const PositionsTable = ({onError}) => {
  const [positions, setPositions] = useState([])
  const [pageSize, setPageSize] = useState(5)
  const [rows, setRows] = useState()
  const [deleteDialogOpen, setDeleteDialogOpen] = useState(false)
  const [closeDialogOpen, setCloseDialogOpen] = useState(false)
  const [addDialogOpen, setAddDialogOpen] = useState(false)
  const [selectedPos, setSelectedPos] = useState()
  const [closurePrice, setClosurePrice] = useState()
  const [closureDate, setClosureDate] = useState()
  const [entryDate, setEntryDate] = useState()
  const [values, setValues] = useState()

  useState(() => {
    doGet("/api/position")
      .then(response => response.json())
      .then(response => {
      const objects = response.content
        setPositions(response.content)
      })
      .catch(() => onError())
  }, [])

  const getAll = () => {
    doGet("/api/position")
      .then(response => response.json())
      .then(response => {
      const objects = response.content
        setPositions(response.content)
      })
      .catch(() => onError())
  }

  const handleDeleteDialogOpen = (id) => {
    setDeleteDialogOpen(true)
    setSelectedPos(id)
  }

  const handleCloseDialogOpen = ({id, entryDate}) => {
    setCloseDialogOpen(true)
    setSelectedPos(id)
    setEntryDate(entryDate)
  }

  const handleAddDialogOpen = () => {
    setAddDialogOpen(true)
  }

  const deletePosition = (id) => {
    const reqUrl = `/api/position/${id}`;
    setPositions(positions.filter((row) => row.id !== id));
    doDelete(reqUrl)
    .then(() => getAll())
  };

  const closePosition = (id, closurePrice, closureDate) => {
    const reqUrl = `/api/position`;
    doPatch(reqUrl, {id: id, closurePrice: closurePrice, closureDate: closureDate})
    .then(() => getAll())
  };

  const addPosition = (name, quantity, entryPrice, entryDate) => {
           console.log("addPosition")
           console.log(name)
           console.log(quantity)
           console.log(entryPrice)
           console.log(entryDate)
    const reqUrl = `/api/position`;
    doPost(reqUrl, {cryptocurrencyName: name, entryPrice: entryPrice, quantity: quantity, entryDate: entryDate})
    .then(() => getAll())
  }

  const positionColumns = React.useMemo(
    () => [
        { field: 'name', headerName: 'Name', type:'string', flex: 1, hideable: false, headerAlign: 'center', align: 'center', minWidth: 100,
          valueGetter: (params) => params.row.cryptocurrency.name},
        { field: 'symbol', headerName: 'Symbol', type:'string', flex: 1, hideable: false, headerAlign: 'center', align: 'center', minWidth: 100,
          valueGetter: (params) => params.row.cryptocurrency.symbol},
        { field: 'quantity', headerName: 'Quantity', type:'number', flex: 1, headerAlign: 'center', align: 'center', minWidth: 100},
        { field: 'entryPrice', headerName: 'Entry Price', type:'number', flex: 1, headerAlign: 'center', minWidth: 100,
          valueFormatter: ({ value }) => currencyFormatter.format(value)},
        { field: 'entryDate', headerName: 'Entry Date', type:'date', flex: 1, headerAlign: 'center', align: 'center', minWidth: 100},
        { field: 'closurePrice', headerName: 'Closure Price', type:'number', flex: 1, headerAlign: 'center', minWidth: 100,
          valueGetter: (params) => {
            if(params.row.closurePrice === (-1.0)) {
              return ""
            }
            return `${params.row.closurePrice}`
            },
          valueFormatter: ({ value }) => currencyFormatter.format(value)
          },
        { field: 'closureDate', headerName: 'Closure Date', type:'date', flex: 1, headerAlign: 'center', align: 'center', minWidth: 100},
        {
          field: 'profit',
          headerName: 'Profit',
          type:'number',
          headerAlign: 'center',
          minWidth: 50,
          valueGetter: (params) => {
            if(params.row.closurePrice === (-1.0)) {
              return ""
            }
            return `${(params.row.closurePrice) - (params.row.entryPrice)}`
          },
          valueFormatter: ({ value }) => currencyFormatter.format(value)
        },
        {field: 'actions', headerName: 'Actions', type: 'actions', flex: 1, hideable: false, headerAlign: 'center', minWidth: 100,
         getActions: (params) => {
           const isOpen = params.row.closureDate === null;
           const id = params.row.id;
           const entryDate = params.row.entryDate;

           if(isOpen) {
             return [
             <GridActionsCellItem
               icon={<DeleteIcon />}
               label="Delete"
               onClick={() => handleDeleteDialogOpen(id)}
               showInMenu
              />,
             <GridActionsCellItem
                icon={<MonetizationOnIcon />}
                label="Close"
                onClick={() => handleCloseDialogOpen({id, entryDate})}
                showInMenu
               />,
             ];
           }

           return [
            <GridActionsCellItem
             icon={<DeleteIcon />}
             label="Delete"
             onClick={() => handleDeleteDialogOpen(id)}
             showInMenu
             />,
           ];
         },
       },
    ],
);

  const currencyFormatter = new Intl.NumberFormat('en-US', {
    style: 'currency',
    currency: 'USD',
  });

  return (
      <Box>
        <DeleteDialog open={deleteDialogOpen} onClose={() => setDeleteDialogOpen(false)} rowId={selectedPos} deletePosition={() => deletePosition(selectedPos)}/>
        <CloseDialog open={closeDialogOpen} onClose={() => setCloseDialogOpen(false)} rowId={selectedPos} entryDate={entryDate}
          closePosition={closePosition}/>
        <AddDialog open={addDialogOpen} onClose={() => setAddDialogOpen(false)} addPosition={addPosition}/>
        <Box sx={{ height: '400px', display: 'flex', flexdirection: 'row', justifyContent: 'center'}}>
          <DataGrid
           rows={positions}
           columns={positionColumns}
           pageSize={5}
           rowsPerPageOptions={[5, 10, 20]}
           pagination
           pageSize={pageSize}
           onPageSizeChange={(newPageSize) => setPageSize(newPageSize)}
           sx={{color:"#9AA5C5"}}
           loading={!positions.length}
          />
        </Box>
        <Box sx={{ mt: '50px'}}>
          <Button
           variant="contained"
           onClick={() => handleAddDialogOpen()}
           size="large"
           sx={{margintop:"50px"}}
           >
            Add new position
          </Button>
        </Box>
      </Box>

  )
}

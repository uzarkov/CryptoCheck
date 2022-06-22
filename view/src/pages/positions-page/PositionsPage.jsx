import {useEffect, useState} from "react";
import {doGet, doPost, doDelete} from "../../utils/fetch-utils";
import {Parse} from '@parse/react';
import { Snackbar } from '@mui/material';
import { Alert } from '@mui/material';
import Box from '@mui/material/Box';
import Button from '@mui/material/Button';
import Typography from '@mui/material/Typography';
import Modal from '@mui/material/Modal';
import {PositionsTable} from "../../components/positions/positionsTable";


export const URL = '/positions';
export const NAME = "Positions";

export const PositionsPage = () => {
  const isMobile = window.matchMedia(`(max-width: 720px)`).matches;
  const [positions, setPositions] = useState([])
  const [open, setOpen] = useState(false)


  const handleCLick = () => {
    setOpen(true)
  }

  const handleClose = (event, reason) => {
    if(reason === 'clickaway') {
      return
    }
    setOpen(false)
  }

  return (
    <div style={isMobile ? {width: "max-content", paddingRight: 16} : {}}>
      <h1>
        Positions Page
      </h1>

        <PositionsTable onError={() => setOpen(true)}/>

      <Snackbar
        open={open}
        onClose={handleClose}
        anchorOrigin={{ vertical: 'top', horizontal: 'center' }}>
        <Alert onClose={handleClose} severity="error" sx={{ width: '100%' }}>
          Couldn't fetch data
        </Alert>
      </Snackbar>
    </div>
  )
}
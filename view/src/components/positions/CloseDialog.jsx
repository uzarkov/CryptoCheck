import * as React from 'react';
import {useEffect, useState} from "react";
import Button from '@mui/material/Button';
import Dialog from '@mui/material/Dialog';
import DialogActions from '@mui/material/DialogActions';
import DialogContent from '@mui/material/DialogContent';
import DialogContentText from '@mui/material/DialogContentText';
import DialogTitle from '@mui/material/DialogTitle';
import useMediaQuery from '@mui/material/useMediaQuery';
import TextField from '@mui/material/TextField';
import { useTheme } from '@mui/material/styles';
import { useForm } from 'react-hook-form';
import { yupResolver } from '@hookform/resolvers/yup';
import * as Yup from 'yup'


export const CloseDialog = ({open, onClose, rowId, entryDate, closePosition}) => {
  const date = new Date()
  const currentDate = date.toLocaleDateString('en-CA')

  const defaultInputValues = {
    closurePrice: 0,
    closureDate: currentDate
  };

  const theme = useTheme();
  const fullScreen = useMediaQuery(theme.breakpoints.down('md'));
  const [values, setValues] = useState([])
//   const [entryDateCheck, setEntryDateCheck] = useState()

  useEffect(() => {
    if(open) setValues(defaultInputValues);
  }, [open])

  const validationSchema = Yup.object().shape({
      closurePrice: Yup.number()
      .min(0, 'Min value is 0.')
      .required('You must specify a closure price.'),
      closureDate: Yup.date()
//       .min(entryDateCheck, "Closure date cannot be earlier than entry date.")
      .required('You must specify a closure date.')
    });

  const { register, handleSubmit, formState: { errors } } = useForm({
        resolver: yupResolver(validationSchema)
  });

  const handleChange = (value) => {
   console.log("value change")
    setValues(value)
  };

  const handleClosePosition = (rowId, closurePrice, closureDate) => {
    closePosition(rowId, closurePrice, closureDate)
    onClose()
  }

  return (
    <div>
      <Dialog
        open={open}
        onClose={onClose}
        aria-labelledby="responsive-dialog-title"
       >
        <DialogTitle>Close Position</DialogTitle>
        <DialogContent>
          <DialogContentText>
            Enter closure price and closure date of this position
          </DialogContentText>
          <form onSubmit={handleSubmit(() => handleClosePosition(rowId, values.closurePrice, values.closureDate))}>
          <TextField
           type='number'
           autoFocus
           step='any'
           placeholder='Closure Price'
           name='closurePrice'
           label='Closure Price'
           required
           margin="dense"
           fullWidth
           variant="standard"
           {...register('closurePrice')}
           error={errors.closurePrice ? true : false}
           helperText={errors.closurePrice?.message}
           value={values.closurePrice}
           onChange={(event) => handleChange({ ...values, closurePrice: event.target.value})}
           />
          <TextField
           type='date'
           autoFocus
           placeholder='Closure Date'
           name='closureDate'
           label='Closure Date'
           required
           margin="dense"
           fullWidth
           variant="standard"
           {...register('closureDate')}
           error={errors.closureDate ? true : false}
           helperText={errors.closureDate?.message}
           value={values.closureDate}
           onChange={(event) => handleChange({ ...values, closureDate: event.target.value})}
           />
           <DialogActions>
             <Button onClick={onClose}>Cancel</Button>
             <Button
              type="submit"
              variant="contained"
              color="primary"
              >Close position</Button>
             </DialogActions>
          </form>
        </DialogContent>
      </Dialog>
    </div>
  );
}
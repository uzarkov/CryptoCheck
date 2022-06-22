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


export const AddDialog = ({open, onClose, addPosition}) => {
  const date = new Date()
  const currentDate = date.toLocaleDateString('en-CA')

  const defaultInputValues = {
    name: '',
    quantity: 0,
    entryPrice: 0,
    entryDate: currentDate
  };

  const theme = useTheme();
  const fullScreen = useMediaQuery(theme.breakpoints.down('md'));
  const [values, setValues] = useState([])
//   const [entryDateCheck, setEntryDateCheck] = useState()

  useEffect(() => {
    if(open) setValues(defaultInputValues);
  }, [open])

  const validationSchema = Yup.object().shape({
      name: Yup.string()
      .required("You must specify valid cryptocurrency name."),
      quantity: Yup.number()
      .required('You must specify a quantity.')
      .min(0, 'Min value is 0.'),
      entryPrice: Yup.number()
      .required('You must specify an entry price.')
      .min(0, 'Min value is 0.'),
      entryDate: Yup.date()
      .required("You must specify an entry date."),
    });

  const { register, handleSubmit, formState: { errors } } = useForm({
        resolver: yupResolver(validationSchema)
  });

  const handleChange = (value) => {
    setValues(value)
  };

  const handleAddPosition = (name, quantity, entryPrice, entryDate) => {
    addPosition(name, quantity, entryPrice, entryDate)
    onClose()
  }

  return (
    <div>
      <Dialog
        open={open}
        onClose={onClose}
        aria-labelledby="responsive-dialog-title"
       >
        <DialogTitle>Add position</DialogTitle>
        <DialogContent>
          <DialogContentText>
            Enter name of crypto, quantity, entry price adn entry date
          </DialogContentText>
          <form onSubmit={handleSubmit(() => handleAddPosition(values.name, values.quantity, values.entryPrice, values.entryDate))}>
           <TextField
            type='string'
            autoFocus
            margin="dense"
            step='any'
            placeholder='Name'
            name='name'
            label='Cryptocurrency name'
            required
            fullWidth
            variant="standard"
            {...register('name')}
            error={errors.name ? true : false}
            helperText={errors.name?.message}
            value={values.name}
            onChange={(event) => handleChange({ ...values, name: event.target.value})}
           />
          <TextField
            type='number'
            autoFocus
            step='any'
            margin="dense"
            fullWidth
            type='number'
            step='any'
            placeholder='Quantity'
            name='quantity'
            label='Quantity'
            required
            variant="standard"
            {...register('quantity')}
            error={errors.quantity ? true : false}
            helperText={errors.quantity?.message}
            value={values.quantity}
            onChange={(event) => handleChange({ ...values, quantity: event.target.value})}
           />
          <TextField
           type='number'
           autoFocus
           step='any'
           placeholder='Entry Price'
           name='entryPrice'
           label='Entry Price'
           required
           margin="dense"
           fullWidth
           variant="standard"
           {...register('entryPrice')}
           error={errors.entryPrice ? true : false}
           helperText={errors.entryPrice?.message}
           value={values.entryPrice}
           onChange={(event) => handleChange({ ...values, entryPrice: event.target.value})}
           />
          <TextField
           type='date'
           autoFocus
           placeholder='Entry Date'
           name='entryDate'
           label='Entry Date'
           required
           margin="dense"
           fullWidth
           variant="standard"
           {...register('entryDate')}
           error={errors.entryDate ? true : false}
           helperText={errors.entryDate?.message}
           value={values.entryDate}
           onChange={(event) => handleChange({ ...values, entryDate: event.target.value})}
           />
           <DialogActions>
           <Button onClick={onClose}>Cancel</Button>
           <Button
            type="submit"
            variant="contained"
            color="primary"
            >Add position</Button>
           </DialogActions>
          </form>
        </DialogContent>
      </Dialog>
    </div>
  );
}
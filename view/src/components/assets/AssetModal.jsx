import Box from '@mui/material/Box';
import Typography from '@mui/material/Typography';
import Modal from '@mui/material/Modal';
import Avatar from "@mui/material/Avatar";

const style = {
  position: 'absolute',
  top: '50%',
  left: '50%',
  transform: 'translate(-50%, -50%)',
  width: 400,
  bgcolor: 'background.paper',
  border: '0px solid #000',
  borderRadius: 4,
  boxShadow: 24,
  p: 4,
};

export const AssetModal = ({asset, isOpen, onClose}) => {
  if (!isOpen) {
    return null;
  }

  return (
    <Modal
      open={isOpen}
      onClose={() => onClose(asset)}
    >
      <Box sx={style}>
        <Avatar src={asset.icon} />
        <Typography>
          {asset.name}
        </Typography>
      </Box>
    </Modal>
  )
}
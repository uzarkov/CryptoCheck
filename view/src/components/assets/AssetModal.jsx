import Box from '@mui/material/Box';
import Modal from '@mui/material/Modal';
import {AssetChartContainer} from "./AssetChartContainer";

const isMobile = window.matchMedia(`(max-width: 720px)`).matches;

const style = {
  position: 'absolute',
  top: '50%',
  left: '50%',
  transform: 'translate(-50%, -50%)',
  width: isMobile ? "95%" : 800,
  height: isMobile ? "fit-content" : 500,
  bgcolor: 'background.paper',
  border: '0px solid #000',
  borderRadius: 4,
  boxShadow: 24,
  p: 2,
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
        <AssetChartContainer asset={asset} />
      </Box>
    </Modal>
  )
}
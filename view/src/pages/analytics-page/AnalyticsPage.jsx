import {useEffect, useState} from "react";
import {doGet} from "../../utils/fetch-utils";
import {AssetTable} from "../../components/assets/AssetsTable";
import {AssetModal} from "../../components/assets/AssetModal";

export const URL = '/analytics';
export const NAME = "Analytics";

export const AnalyticsPage = () => {
    const isMobile = window.matchMedia(`(max-width: 720px)`).matches;

    const [assets, setAssets] = useState([])
    const [modalsState, setModalsState] = useState({})

    useEffect(() => {
      doGet("/api/cryptocurrency?size=25")
        .then(response => response.json())
        .then(assetPage => {
          setAssets(assetPage.content)
          const initialModalsState = {}
          assetPage.content.forEach(asset => initialModalsState[asset.symbol] = null)
          setModalsState(initialModalsState)
        })
        .catch(error => console.error(error))
    }, [])

    const openModal = (asset) => {
      setModalsState({
        ...modalsState,
        [asset.symbol]: asset
      })
    }

    const closeModal = (asset) => {
      setModalsState({
        ...modalsState,
        [asset.symbol]: null
      })
    }

    return (
        <div style={isMobile ? {width: "max-content", paddingRight: 16} : {}}>
          <AssetTable assetsMetadata={assets} onRowClick={openModal} />
          {Object.values(modalsState)
            .filter(asset => asset !== null)
            .map((asset, idx) => <AssetModal key={idx} asset={asset} isOpen={true} onClose={closeModal} />)}
        </div>
    )
}
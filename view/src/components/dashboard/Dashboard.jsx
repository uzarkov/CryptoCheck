import {useEffect, useState} from "react";
import {AbbreviatedPositionsTable} from "./abbreviated-positions-table/AbbreviatedPositionTable";
import {DoughnutChart} from "./pie-chart/DoughnutChart";
import "../../global-styles/globalStyles.css";
import "./Dashboard.css";
import {doGet} from "../../utils/fetch-utils";
import {CryptoPreferences} from "./crypto-preferences/CryptoPreferences";

export const Dashboard = () => {

    const getCoinInfoUrl = (coingeckoCoinId) =>
        `https://api.coingecko.com/api/v3/coins/${coingeckoCoinId}?tickers=false&market_data=true&community_data=false&developer_data=false&sparkline=false`

    const [preferences, setPreferences] = useState([]);
    const [icons, setIcons] = useState([]);
    const [portfolio, setPortfolio] = useState({})
    const [positions, setPositions] = useState([])

    const [dataFetched, setDataFetched] = useState(false);
    const [iconsFetched, setIconsFetched] = useState(false);

    useEffect(() => {
        setDataFetched(false);
        doGet("/api/dashboard")
            .then(response => response.json())
            .then(data => {
                const {preferences, positions, portfolio} = data
                setPortfolio(portfolio)
                setPreferences(preferences)
                setPositions(positions)
                setDataFetched(true);
            })
            .catch(error => console.error(error))
    }, [])

    useEffect(() => {
        setIconsFetched(false);
        const promises = [...preferences].map(pref => {
            return doGet(getCoinInfoUrl(pref.cryptocurrencyOutput.coingeckoId))
                .then(response => response.json())
                .then(coingeckoData => ({
                    symbol: pref.cryptocurrencyOutput.symbol,
                    icon: coingeckoData.image.large
                }))
                .catch(error => {
                    console.log('some errorx')
                    console.error(error);
                    return undefined;
                })
        })

        Promise.all(promises)
            .then(results => results.filter(res => res !== undefined))
            .then(results => {
                setIcons(results);
                setIconsFetched(true);
            })
            .catch(error => console.log(error))
    },[dataFetched])

    const handleUpdatingPreferences = (reorderedPreferences) => {
        setPreferences(reorderedPreferences);
    }

    return (
        <>
            {(dataFetched && iconsFetched)
                ?
                <div className={"container shadowbox"}>
                    <CryptoPreferences
                        preferences={preferences}
                        setPreferences={handleUpdatingPreferences}
                        icons={icons}
                    />
                    <div className={"userInfo"}>
                        <AbbreviatedPositionsTable
                            positions={positions}
                        />
                        <DoughnutChart
                            assets={portfolio.assets}
                            totalValue={portfolio.totalValue}
                        />
                    </div>
                </div>
                :
                <h1 className={"loadingInfo"}>
                    Loading...
                </h1>
            }
        </>
    );
}
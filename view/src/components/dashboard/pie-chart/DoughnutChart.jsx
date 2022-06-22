import "../../../global-styles/globalStyles.css";
import "./DoughnutChart.css";

import PieChart, {Series, Label, Legend, Connector,} from 'devextreme-react/pie-chart'
import {CenterTemplate} from './CenterTemplate'
import {Link} from "react-router-dom";


export const DoughnutChart = ({assets, totalValue}) => {

    return (
        <div className={"shadowbox"}>
            <label className={"title"}>
                Portfolio
            </label>
            <div className={"chartContainer"}>
                <PieChart
                    id={"pie-chart"}
                    dataSource={assets}
                    resolveLabelOverlapping={"shift"}
                    sizeGroup={"piesGroup"}
                    innerRadius={0.75}
                    centerRender={(e) => CenterTemplate(e, totalValue)}
                    type={"doughnut"}
                    height={350}
                >
                    <Series
                        argumentField={"cryptocurrencySymbol"}
                        valueField={"percentageShare"}
                    >
                        <Label
                            visible={true}
                            format={"fixedPoint"}
                            customizeText={(e) => `${e.argumentText} ${e.valueText}%`}
                            backgroundColor={"none"}
                            font={{size: '16', color: 'black', weight: 'bold'}}
                        >
                            <Connector visible={true}/>
                        </Label>
                    </Series>
                    <Legend visible={false}/>
                </PieChart>
                <button className={"linkButton"}>
                    <Link
                        to={"/portfolio"}
                        className={"linkButton"}
                    >
                        View more
                    </Link>
                </button>
            </div>
        </div>
    );
}
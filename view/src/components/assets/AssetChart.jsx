import {useEffect} from "react";
import { Chart, LineController, LineElement, PointElement, LinearScale, CategoryScale, Title, Tooltip } from "chart.js";
import moment from "moment/moment";

Chart.register(LineController, LineElement, PointElement, LinearScale, CategoryScale, Title, Tooltip);

export const AssetChart = ({asset, data}) => {
  useEffect(() => {
    const chart = renderChartFor(asset, data)
    return () => {
      chart.destroy()
    }
  }, [asset, data])

  return null;
}

const renderChartFor = (asset, data) => {
  const chartConfig = {
    type: "line",
    data: {
      labels: data.map(candlestick => candlestick.openTime),
      datasets: [{
        data: data.map(candlestick => candlestick.closePrice),
        borderColor: "#76ccf5",
        cubicInterpolationMode: 'monotone',
        fill: false
      }]
    },
    options: {
      responsive: true,
      elements: {
        point: {
          radius: 0,
          pointHoverRadius: 0,
        },
        line: {
          borderWidth: 2,
        }
      },
      interaction: {
        intersect: false,
        mode: 'nearest',
        axis: 'x',
      },
      scales: {
        x: {
          ticks: {
            display: false
          },
          grid: {
            display: false
          }
        },
      },
      plugins: {
        legend: {
          display: false,
        },
        title: {
          display: false,
        },
        tooltip: {
          callbacks: {
            title: ctx => {
              const openTime = ctx[0].label
              return moment(openTime, "x").format("yyyy-MM-DD hh:mm")
            }
          }
        }
      }
    },
  }

  const ctx = document.getElementById(`${asset.symbol}-chart`).getContext('2d')
  return new Chart(ctx, chartConfig)
}
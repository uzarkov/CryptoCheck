import { useHistory } from 'react-router-dom';

export const URL = '/dashboard';

export const DashboardPage = () => {

    const history = useHistory();

    return (
        <div>
            <h1>
                Dashboard Page
            </h1>
            <button onClick={() => history.push("/analytics")}>
                go to analytics
            </button>
            <button onClick={() => history.push("/portfolio")}>
                go to portfolio
            </button>
            <button onClick={() => history.push("/positions")}>
                go to positions
            </button>
            <button onClick={() => history.push("*")}>
                go to error page
            </button>

        </div>
    )
}
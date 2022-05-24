import { useHistory } from 'react-router-dom';

export const URL = '/analytics';

export const AnalyticsPage = () => {

    const history = useHistory();

    return (
        <div>
            <h1>
                Analytics Page
            </h1>
            <button onClick={() => history.push("/")}>
                return to dashboard
            </button>
        </div>
    )
}
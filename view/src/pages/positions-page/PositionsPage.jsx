import { useHistory } from 'react-router-dom';

export const URL = '/positions';

export const PositionsPage = () => {

    const history = useHistory();

    return (
        <div>
            <h1>
                Positions Page
            </h1>
            <button onClick={() => history.push("/")}>
                return to dashboard
            </button>
        </div>
    )
}
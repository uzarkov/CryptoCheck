import { useHistory } from 'react-router-dom';

export const URL = '/portfolio';

export const PortfolioPage = () => {

    const history = useHistory();

    return (
        <div>
            <h1>
                Portfolio Page
            </h1>
            <button onClick={() => history.push("/")}>
                return to dashboard
            </button>
        </div>
    );
}
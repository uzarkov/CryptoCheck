import { useHistory } from 'react-router-dom';

export const ErrorPage = () => {

    const history = useHistory();

    return (
        <div>
            <h1>
                Error Page
            </h1>
            <button onClick={() => history.push("/")}>
                return to dashboard
            </button>
        </div>
    )
}
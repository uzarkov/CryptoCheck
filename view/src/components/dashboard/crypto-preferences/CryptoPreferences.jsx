import Button from "@mui/material/Button";
import {ArrowBackIos, ArrowForwardIos} from "@mui/icons-material";
import {CryptoPreference} from "./crypto-preference/CryptoPreference";
import "./CryptoPreferences.css"
import {useEffect, useState} from "react";

export const CryptoPreferences = ({preferences, setPreferences, icons}) => {

    const [preferencesAmount, setPreferencesAmount] = useState(0)

    useEffect(() => {
        const prefAmount = handleResize();
        setPreferencesAmount(prefAmount);
        window.addEventListener("resize", handleResize, false);
    }, [])

    const getImage = (symbol) => {
       return icons.filter(icon => icon.symbol === symbol)[0].icon;
    }

    const gcd = (n, k) => {
        if(n === 0){
            return k;
        }
        if(k === 0){
            return n;
        }
        if(n > k){
            return gcd(n - k, k);
        }
        else{
            return gcd(n,k - n);
        }
    }

    const juggle = (n, k) => {
        const arr = [...preferences];
        const g = gcd(n,k);
        let temp,m,j;

        for (let i=0; i<g; i++){
            temp=arr[i];
            j = i;
            while(true){
                m = j + k;
                if(m >= n){
                    m= m - n;
                }
                if(m === i){
                    break;
                }
                arr[j] = arr[m];
                j = m;
            }
            arr[j] = temp;
        }
        return arr;
    }

    const shiftLeft = () => {
        const arr = juggle(preferences.length, 1);
        setPreferences(arr);
    }

    const shiftRight = () => {
        const arr = juggle(preferences.length, preferences.length - 1);
        setPreferences(arr);
    }

    const handleResize = () => {

        let prefAmount;
        switch (true) {
            case (window.innerWidth > 1865):
                prefAmount = 8;
                break;
            case (window.innerWidth > 1685):
                prefAmount = 7;
                break;
            case (window.innerWidth > 1500):
                prefAmount = 6;
                break;
            case (window.innerWidth > 1330):
                prefAmount = 5;
                break;
            case (window.innerWidth > 1140):
                prefAmount = 4;
                break;
            case (window.innerWidth > 805):
                prefAmount = 3;
                break;
            case (window.innerWidth > 540):
                prefAmount = 2;
                break;
            default:
                prefAmount = 1;
        }
        setPreferencesAmount(prefAmount)
        return prefAmount;
    }

    return (
        <div className={"preferences"}>
            <Button
                variant={"text"}
                onClick={() => shiftLeft()}
            >
                <ArrowBackIos />
            </Button>
            {preferences.slice(0, preferencesAmount).map(value => {
                return (
                    <CryptoPreference
                        key={value.id.cryptocurrencyId}
                        symbol={value.cryptocurrencyOutput.symbol}
                        price={value.price}
                        icon={getImage(value.cryptocurrencyOutput.symbol)}
                        change={value.change}
                    />);
            })}
            <Button
                variant={"text"}
                onClick={() => shiftRight()}
            >
                <ArrowForwardIos/>
            </Button>
        </div>
    )
}
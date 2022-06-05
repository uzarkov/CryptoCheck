export const fixedDecimals = (priceAsString) => {
  const asFloat = parseFloat(priceAsString)

  if (asFloat < 0.001) {
    return asFloat.toFixed(8).toString()
  }

  if (asFloat < 0.1) {
    return asFloat.toFixed(6).toString()
  }

  if (asFloat < 1) {
    return asFloat.toFixed(4).toString()
  }

  return prettyPrint(asFloat.toFixed(2).toString())
}

export const prettyPrint = (priceAsString) => {
  return priceAsString.replace(/\B(?=(\d{3})+(?!\d))/g, " ")
}
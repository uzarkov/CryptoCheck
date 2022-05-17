export const URL_PREFIX = process.env.NODE_ENV === 'development' ? 'http://localhost:8080' : '';

const checkForError = async (response) => {
  if (!response.ok) {
    if (response.headers.get('content-type') === 'application/json') {
      const json = await response.json()
      throw new Error(json.message)
    }
    throw new Error("Something went wrong")
  }
  return response
}

export const doGet = async (endpoint) => {
  const headers = await addAuthorizationHeader({
    'Accept': 'application/json',
  })

  return fetch(URL_PREFIX + endpoint, {
    method: 'GET',
    headers: headers,
  })
    .then(response => checkForError(response))
}

export const doPost = async (endpoint, body = {}, authorize = true) => {
  let headers = {
    'Content-Type': 'application/json',
    'Accept': 'application/json',
  }

  if (authorize) {
    headers = await addAuthorizationHeader(headers)
  }

  return fetch(URL_PREFIX + endpoint, {
    method: 'POST',
    headers: headers,
    body: JSON.stringify(body)
  })
    .then(response => checkForError(response))
}

export const doPatch = async (endpoint, body = {}) => {
  const headers = await addAuthorizationHeader({
    'Content-Type': 'application/json',
    'Accept': 'application/json',
  })

  return fetch(URL_PREFIX + endpoint, {
    method: 'PATCH',
    headers: headers,
    body: JSON.stringify(body)
  })
    .then(response => checkForError(response))
}

export const doDelete = async (endpoint) => {
  const headers = await addAuthorizationHeader({
    'Accept': 'application/json',
  })

  return fetch(URL_PREFIX + endpoint, {
    method: 'DELETE',
    headers: headers,
  })
    .then(response => checkForError(response))
}

const addAuthorizationHeader = async (headers) => {
  const jwt = localStorage.getItem("ACCESS_TOKEN")

  if (jwt) {
    return {
      ...headers,
      ['Authorization']: `Bearer ${jwt}`,
    }
  }

  return headers
}
import { createContext, useCallback, useContext, useState } from 'react'

const AppContext = createContext(null)

export function AppContextProvider({ children }) {
  const [alert, setAlert] = useState(null)

  const showAlert = useCallback((type, message) => {
    setAlert({ type, message, id: Date.now() })
  }, [])

  const clearAlert = useCallback(() => setAlert(null), [])

  const value = { alert, showAlert, clearAlert }
  return <AppContext.Provider value={value}>{children}</AppContext.Provider>
}

export function useAppContext() {
  const ctx = useContext(AppContext)
  if (!ctx) {
    throw new Error('useAppContext must be used inside AppContextProvider')
  }
  return ctx
}

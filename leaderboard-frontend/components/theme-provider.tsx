"use client"

import * as React from "react"
import { ThemeProvider as NextThemesProvider } from "next-themes"
import type { ThemeProviderProps } from "next-themes"

export function ThemeProvider({ children, ...props }: ThemeProviderProps) {
  const [mounted, setMounted] = React.useState(false)

  // Only render the theme provider after the component has mounted
  // This prevents hydration mismatch
  React.useEffect(() => {
    setMounted(true)
  }, [])

  // If not mounted yet, render children without theme context
  // This ensures server and client render the same initial HTML
  if (!mounted) {
    return <>{children}</>
  }

  return <NextThemesProvider {...props}>{children}</NextThemesProvider>
}
